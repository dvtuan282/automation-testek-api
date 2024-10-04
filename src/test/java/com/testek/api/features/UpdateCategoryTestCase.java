package com.testek.api.features;

import com.testek.api.models.AccountModel;
import com.testek.api.models.CategoryModel;
import com.testek.api.questions.BodyResponse;
import com.testek.api.questions.StatusCodeResponse;
import com.testek.api.tasks.*;
import com.testek.api.utilities.CategoryEndpoints;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
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
    @MethodSource("DataUpdateCateSuccess")
    void UpdateCateSuccess(CategoryModel categoryUpdate, String description) {

        System.out.println("=======Create data=======");
        actor.attemptsTo(
                CreateCategoryTask.withCategory(new CategoryModel("tạo ra để update", "Update5s4", "ACTIVE"))
        );
        // lấy id từ response vừa tạo để cập nhật
        String pathParam = actor.asksFor(BodyResponse.bodyResponse("data.id")).toString();
        System.out.println("=======Update data=======");
        actor.attemptsTo(
                UpdateCategoryTask.updateCategoryWith(pathParam, categoryUpdate),
                Ensure.that(description, StatusCodeResponse.responseStatus()).isEqualTo(200)
        );
        System.out.println("=======Compare data=======");
        actor.attemptsTo(
                GetCategoryTask.withCategoryId(pathParam),
                Ensure.that(description, BodyResponse.bodyResponse("data.cateDesc").asString()).isEqualTo(categoryUpdate.getCateDesc()),
                Ensure.that(description, BodyResponse.bodyResponse("data.status").asString()).isEqualTo(categoryUpdate.getStatus())
        );
        System.out.println("=======Clean data=======");
        actor.attemptsTo(
                DeleteCategoryTask.deleteCategory(pathParam, false)
        );
    }

//    @ParameterizedTest
//    @MethodSource("DataUpdateCateNotSuccess")
//    void UpdateCateNotSuccess(CategoryModel categoryModel, String description, String errorExpected) {
//        System.out.println("=======Create data=======");
//        actor.attemptsTo(
//                CreateCategoryTask.withCategory(new CategoryModel("tạo ra để update", "UpdateNoSuccess", "ACTIVE"))
//        );
//        // lấy id từ response vừa tạo để cập nhật
//        String pathParam = actor.asksFor(BodyResponse.bodyResponse("data.id")).toString();
//        System.out.println("=======Update data=======");
//        actor.attemptsTo(
//                UpdateCategoryTask.updateCategoryWith(pathParam, categoryModel),
//                Ensure.that(description, StatusCodeResponse.responseStatus()).isEqualTo(400),
//                Ensure.that(description, BodyResponse.bodyResponse("error").asString()).isEqualTo(errorExpected)
//        );
//    }


    private static Stream<Arguments> DataUpdateCateSuccess() {
        return Stream.of(
                Arguments.of(new CategoryModel("Dòng xe địa hình cao cấp", "tuanTester3", "ACTIVE"), "Tạo danh mục active thành công"),
                Arguments.of(new CategoryModel("Dòng xe địa hình cao cấp", "tuantester4", "INACTIVE"), "Tạo danh mục inactive thành công")
        );
    }

//    private static Stream<Arguments> DataUpdateCateNotSuccess() {
//        return Stream.of(
//                Arguments.of(new CategoryModel("Dòng xe đạp cho người già", null, "ACTIVE"), "Tạo danh mục thành công khi để trống các trường require", "INVALID_INPUT"),
//                Arguments.of(new CategoryModel("Dòng xe đạp đường phố", " ", "ACTIVE"), "Tạo danh mục thành công khi để trống các trường require", "INVALID_INPUT"),
//                Arguments.of(new CategoryModel("Dòng xe địa hình cao cấp copy", "tuanTester1", "ACTIVE"), "TTạo danh mục với tên đã tồn tại", "CATEGORY_EXISTED")
//        );
//    }
}
