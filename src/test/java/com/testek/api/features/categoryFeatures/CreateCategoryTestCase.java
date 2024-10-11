package com.testek.api.features.categoryFeatures;

import com.testek.api.models.AccountModel;
import com.testek.api.models.CategoryModel;
import com.testek.api.questions.BodyResponse;
import com.testek.api.questions.CategoryResponse;
import com.testek.api.questions.StatusCodeResponse;
import com.testek.api.tasks.CategoryTasks.CreateCategoryTask;
import com.testek.api.tasks.CategoryTasks.DeleteCategoryTask;
import com.testek.api.tasks.CategoryTasks.GetDetailCategoryTask;
import com.testek.api.tasks.LoginTask;
import com.testek.api.utilities.Endpoints;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

@ExtendWith(SerenityJUnit5Extension.class)
public class CreateCategoryTestCase {
    private static Actor actor;
    private String categoryId;

    @BeforeAll
    static void setUp() {
        actor = Actor.named("tuanTester").whoCan(CallAnApi.at(Endpoints.BASIC_URL));
    }

    @BeforeEach
    void login() {
        actor.attemptsTo(
                LoginTask.withAccount(new AccountModel("testek", "admin"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideCategorySuccess")
    void CreateCateSuccess(CategoryModel categoryRequest) {
        // Tạo mới một category
        actor.attemptsTo(
                CreateCategoryTask.withCategory(categoryRequest),
                Ensure.that("Verify status code: ", StatusCodeResponse.responseStatus()).isEqualTo(201)
        );
        int statusCheck = actor.asksFor(StatusCodeResponse.responseStatus());
        if (statusCheck == 201 || statusCheck == 200) {
            // Lấy id từ response body từ bước tạo category
            categoryId = actor.asksFor(BodyResponse.bodyResponse("data.id")).toString();
        }

        // Lấy chi tiết category vừa tạo
        actor.attemptsTo(
                GetDetailCategoryTask.withCategoryId(categoryId),
                Ensure.that("Verify Category response", CategoryResponse.responseBody(categoryRequest).asString()).isEqualTo("CateRes match CateReq")
        );
    }

    @ParameterizedTest
    @MethodSource("provideCategoryWithNameInvalid")
    void CreateCateWithNameInvalid(CategoryModel categoryRequest) {
        // Tạo mới một category
        actor.attemptsTo(
                CreateCategoryTask.withCategory(categoryRequest),
                Ensure.that("Verify status code: ", StatusCodeResponse.responseStatus()).isEqualTo(400),
                Ensure.that("Verify message: ", BodyResponse.bodyResponse("message").asString()).isEqualTo("Dữ liệu nhập không hợp lệ"),
                Ensure.that("Verify error: ", BodyResponse.bodyResponse("error").asString()).isEqualTo("INVALID_INPUT")
        );
        int statusCheck = actor.asksFor(StatusCodeResponse.responseStatus());
        if (statusCheck == 201 || statusCheck == 200) {
            // Lấy id từ response body từ bước tạo category
            categoryId = actor.asksFor(BodyResponse.bodyResponse("data.id")).toString();
        }
    }

    @Test
    void CreateCateDuplicateName() {
        CategoryModel categoryRequest = new CategoryModel("Create duplicate name", "CTT001", "ACTIVE");
        actor.attemptsTo(
                CreateCategoryTask.withCategory(categoryRequest)
        );
        categoryId = actor.asksFor(BodyResponse.bodyResponse("data.id")).toString();
        actor.attemptsTo(
                CreateCategoryTask.withCategory(categoryRequest),
                Ensure.that("Verify status code: ", StatusCodeResponse.responseStatus()).isEqualTo(400),
                Ensure.that("Verify message: ", BodyResponse.bodyResponse("message").asString()).isEqualTo("Category already exists {0}"),
                Ensure.that("Verify error: ", BodyResponse.bodyResponse("error").asString()).isEqualTo("CATEGORY_EXISTED")
        );
        int statusCheck = actor.asksFor(StatusCodeResponse.responseStatus());
        if (statusCheck == 201 || statusCheck == 200) {
            // Lấy id từ response body từ bước tạo category
            categoryId = actor.asksFor(BodyResponse.bodyResponse("data.id")).toString();
        }

    }
    @AfterEach
    public void cleanUp() {
        if (categoryId != null) {
            actor.attemptsTo(
                    DeleteCategoryTask.withDetails(categoryId, false)
            );
        }

    }

    private static Stream<Arguments> provideCategorySuccess() {
        return Stream.of(
                Arguments.of(new CategoryModel("testcase create category success", "CTT001", "ACTIVE")),
                Arguments.of(new CategoryModel(null, "CTT001", "ACTIVE")),
                Arguments.of(new CategoryModel("testcase create category success", "CTT001", "INACTIVE"))
        );
    }

    private static Stream<Arguments> provideCategoryWithNameInvalid() {
        return Stream.of(
                Arguments.of(new CategoryModel("testcase create category not success", null, "ACTIVE")),
                Arguments.of(new CategoryModel("testcase create category not success", "", "ACTIVE")),
                Arguments.of(new CategoryModel("testcase create category not success", " ", "ACTIVE")),
                Arguments.of(new CategoryModel("testcase create category success", "CTT001", "AHD"))
        );
    }

}
