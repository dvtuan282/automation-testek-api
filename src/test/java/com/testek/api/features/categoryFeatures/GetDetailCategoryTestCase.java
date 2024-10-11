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
public class GetDetailCategoryTestCase {
    private static Actor actor;
    private static String categoryId;
    private CategoryModel categoryRequest;

    @BeforeAll
    static void setUp() {
        actor = Actor.named("tuanTester").whoCan(CallAnApi.at(Endpoints.BASIC_URL));
    }

    @BeforeEach
    void loginAndCreate() {

        actor.attemptsTo(
                LoginTask.withAccount(new AccountModel("testek", "admin"))
        );

    }

    @ParameterizedTest
    @MethodSource("provideCategoryId")
    void getDetailCategoryFalse(String categoryId, int status, String message, String error) {
        actor.attemptsTo(
                GetDetailCategoryTask.withCategoryId(categoryId),
                Ensure.that("Verify status code: ", StatusCodeResponse.responseStatus()).isEqualTo(status),
                Ensure.that("Verify message: ", BodyResponse.bodyResponse("message").asString()).isEqualTo(message),
                Ensure.that("Verify error: ", BodyResponse.bodyResponse("error").asString()).isEqualTo(error)
        );
    }

    @Test
    void getDetailCategorySuccess() {
        CategoryModel categoryRequest = new CategoryModel("Create duplicate name", "CTT001", "ACTIVE");
        actor.attemptsTo(
                CreateCategoryTask.withCategory(categoryRequest)
        );
        categoryId = actor.asksFor(BodyResponse.bodyResponse("data.id")).toString();
        actor.attemptsTo(
                GetDetailCategoryTask.withCategoryId(categoryId),
                Ensure.that("Verify status code: ", StatusCodeResponse.responseStatus()).isEqualTo(200),
                Ensure.that("Verify error: ", CategoryResponse.responseBody(categoryRequest)).isEqualTo("CateRes match CateReq")
        );
    }

    @AfterEach
    public void cleanUp() {
        actor.attemptsTo(
                DeleteCategoryTask.withDetails(categoryId, false)
        );
    }

    private static Stream<Arguments> provideCategoryId() {
        String categoryIdNotFound = "130c930a-62cf-4d9e-b6e2-19b8644b232";
        String categoryIdInvalid = "130c930a-62cf-4d9e-b6e2-19b8645b00a834";
        return Stream.of(
                Arguments.of(categoryIdNotFound, 404, "Category not found: {0}", "CATEGORY_NOT_FOUND"),
                Arguments.of(categoryIdInvalid, 400, "Dữ liệu nhập không hợp lệ", "INVALID_INPUT")
        );
    }
}
