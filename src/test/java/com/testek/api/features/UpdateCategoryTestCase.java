package com.testek.api.features;

import com.testek.api.models.AccountModel;
import com.testek.api.models.CategoryModel;
import com.testek.api.questions.BodyResponse;
import com.testek.api.questions.StatusCodeResponse;
import com.testek.api.tasks.*;
import com.testek.api.utilities.CategoryEndpoints;
import io.cucumber.java.an.E;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

@ExtendWith(SerenityJUnit5Extension.class)
public class UpdateCategoryTestCase {
    private static Actor actor;
    private String pathParam;

    @BeforeAll
    static void setUp() {
        actor = Actor.named("tuanTester").whoCan(CallAnApi.at(CategoryEndpoints.BASIC_URL));
    }

    @BeforeEach
    void login() {
        actor.attemptsTo(
                LoginTask.withAccount(new AccountModel("testek", "admin"))
        );
        // create category
        actor.attemptsTo(
                CreateCategoryTask.withCategory(new CategoryModel("tạo ra để update", "Update để kiểm tra", "ACTIVE"))
        );
        // lấy path từ body đã tạo thành công
        pathParam = actor.asksFor(BodyResponse.bodyResponse("data.id")).toString();

    }

    @ParameterizedTest
    @MethodSource("DataUpdateCateSuccess")
    void UpdateCateSuccess(CategoryModel categoryUpdate, String description) {

        // Update category
        System.out.println("=======Update data=======");
        actor.attemptsTo(
                UpdateCategoryTask.updateCategoryWith(pathParam, categoryUpdate),
                Ensure.that(description, StatusCodeResponse.responseStatus()).isEqualTo(200)
        );
        // Lấy cate đã update để compare dữ liệu update và category được update
        System.out.println("=======Compare data=======");
        actor.attemptsTo(
                GetCategoryTask.withCategoryId(pathParam),
                Ensure.that(description, BodyResponse.bodyResponse("data.cateDesc").asString()).isEqualTo(categoryUpdate.getCateDesc()),
                Ensure.that(description, BodyResponse.bodyResponse("data.status").asString()).isEqualTo(categoryUpdate.getStatus())
        );
    }

    @ParameterizedTest
    @MethodSource("DataUpdateCateNotSuccess")
    void UpdateCateNotSuccess(CategoryModel categoryModel, String description, String errorExpected) {
        System.out.println("=======Update data=======");
        actor.attemptsTo(
                UpdateCategoryTask.updateCategoryWith(pathParam, categoryModel),
                Ensure.that(description, StatusCodeResponse.responseStatus()).isEqualTo(400),
                Ensure.that(description, BodyResponse.bodyResponse("error").asString()).isEqualTo(errorExpected)
        );
    }

    @AfterEach
    void cleanUp() {
        if (pathParam != null) {
            System.out.println("=======Clean data=======");
            actor.attemptsTo(
                    DeleteCategoryTask.deleteCategory(pathParam, false)
            );
        }
    }


    private static Stream<Arguments> DataUpdateCateSuccess() {
        return Stream.of(
                Arguments.of(new CategoryModel("Dòng xe địa hình cao cấp", "tuanTester3", "ACTIVE"), "Tạo danh mục active thành công"),
                Arguments.of(new CategoryModel("Dòng xe địa hình cao cấp", "tuantester4", "INACTIVE"), "Tạo danh mục inactive thành công")
        );
    }

    private static Stream<Arguments> DataUpdateCateNotSuccess() {
        return Stream.of(
                Arguments.of(new CategoryModel("Dòng xe đạp cho người già", null, "ACTIVE"), "Update danh mục khi tên là null", "INVALID_INPUT"),
                Arguments.of(new CategoryModel("Dòng xe đạp đường phố", " ", "ACTIVE"), "Update danh mục khi tên là khoảng trắng", "INVALID_INPUT"),
                Arguments.of(new CategoryModel("Dòng xe địa hình cao cấp copy", "tuanTester1", "ACTIVE"), "Update danh mục khi tên đã tồn tại", "CATEGORY_EXISTED")
        );
    }
}
