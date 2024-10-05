package com.testek.api.features;

import com.testek.api.models.AccountModel;
import com.testek.api.models.CategoryModel;
import com.testek.api.questions.BodyResponse;
import com.testek.api.questions.StatusCodeResponse;
import com.testek.api.tasks.CreateCategoryTask;
import com.testek.api.tasks.DeleteCategoryTask;
import com.testek.api.tasks.GetCategoryTask;
import com.testek.api.tasks.LoginTask;
import com.testek.api.utilities.CategoryEndpoints;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Delete;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

@ExtendWith(SerenityJUnit5Extension.class)
public class CreateCategoryTestCase {
    private static Actor actor;
    private String idActual;

    @BeforeAll
    static void setUp() {
        actor = Actor.named("tuanTester").whoCan(CallAnApi.at(CategoryEndpoints.BASIC_URL));
    }

    @BeforeEach
    void login() {
        actor.attemptsTo(
                LoginTask.withAccount(new AccountModel("testek", "admin"))
        );
    }

    @ParameterizedTest
    @MethodSource("DataCreateCateSuccess")
    void CreateCateSuccess(CategoryModel categoryModel, String description) {
        System.out.println("=======Create data=======");
        actor.attemptsTo(
                CreateCategoryTask.withCategory(categoryModel),
                Ensure.that(description, StatusCodeResponse.responseStatus()).isEqualTo(201)
        );
        idActual = actor.asksFor(BodyResponse.bodyResponse("data.id")).toString();
        System.out.println("idActual: " + idActual);
        System.out.println("=======Compare data=======");
        actor.attemptsTo(
                GetCategoryTask.withCategoryId(idActual),
                Ensure.that(description, BodyResponse.bodyResponse("data.id").asString()).isEqualTo(idActual)
        );
    }

    @ParameterizedTest
    @MethodSource("DataCreateCateNotSuccess")
    void CreateCateNotSuccess(CategoryModel categoryModel, String description, String errorExpected) {
        System.out.println("=======Create data=======");
        actor.attemptsTo(
                CreateCategoryTask.withCategory(categoryModel),
                Ensure.that(description, StatusCodeResponse.responseStatus()).isEqualTo(400),
                Ensure.that(description, BodyResponse.bodyResponse("error").asString()).isEqualTo(errorExpected)
        );
    }

    @AfterEach
    void cleanUp() {
        actor.attemptsTo(
                DeleteCategoryTask.deleteCategory(idActual, false)
        );
    }


    private static Stream<Arguments> DataCreateCateSuccess() {
        return Stream.of(
                Arguments.of(new CategoryModel("Dòng xe địa hình cao cấp", "tuanTester3", "ACTIVE"), "Tạo danh mục active thành công"),
                Arguments.of(new CategoryModel("Dòng xe địa hình cao cấp", "tuantester4", "INACTIVE"), "Tạo danh mục inactive thành công")
        );
    }

    private static Stream<Arguments> DataCreateCateNotSuccess() {
        return Stream.of(
                Arguments.of(new CategoryModel("Dòng xe đạp cho người già", null, "ACTIVE"), "Tạo danh mục thành công khi để trống các trường require", "INVALID_INPUT"),
                Arguments.of(new CategoryModel("Dòng xe đạp đường phố", " ", "ACTIVE"), "Tạo danh mục thành công khi để trống các trường require", "INVALID_INPUT"),
                Arguments.of(new CategoryModel("Dòng xe địa hình cao cấp copy", "tuanTester1", "ACTIVE"), "TTạo danh mục với tên đã tồn tại", "CATEGORY_EXISTED")
        );
    }
}
