package com.testek.api.features.categoryFeatures;

import com.testek.api.models.AccountModel;
import com.testek.api.models.CategoryModel;
import com.testek.api.questions.BodyResponse;
import com.testek.api.questions.CategoryResponse;
import com.testek.api.questions.StatusCodeResponse;
import com.testek.api.tasks.CategoryTasks.CreateCategoryTask;
import com.testek.api.tasks.CategoryTasks.DeleteCategoryTask;
import com.testek.api.tasks.CategoryTasks.GetDetailCategoryTask;
import com.testek.api.tasks.CategoryTasks.UpdateCategoryTask;
import com.testek.api.tasks.LoginTask;
import com.testek.api.utilities.Endpoints;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class DeleteCategoryTestCase {
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
        CategoryModel categoryRequest = new CategoryModel("Create for testcase delete category", "CFTCDC001", "ACTIVE");
        actor.attemptsTo(
                CreateCategoryTask.withCategory(categoryRequest)
        );
        categoryId = actor.asksFor(BodyResponse.bodyResponse("data.id")).toString();
    }


    @ParameterizedTest
    @MethodSource("provideDeleteCategoryModes")
    public void deleteCategory(boolean isSoft, boolean isActive) {
        if (!isActive) {
            CategoryModel categoryUpdate = new CategoryModel("Create for testcase delete category", "CFTCDC001", "INACTIVE");
            actor.attemptsTo(
                    UpdateCategoryTask.withData(categoryId, categoryUpdate)
            );
        }

        performDeleteAndVerify(isSoft);
    }

    private void performDeleteAndVerify(boolean isSoft) {
        actor.attemptsTo(
                DeleteCategoryTask.withDetails(categoryId, isSoft),
                Ensure.that("Verify status code: ", StatusCodeResponse.responseStatus()).isEqualTo(200)
        );

        verifyDeleteOutcome(isSoft);
    }

    private void verifyDeleteOutcome(boolean isSoft) {
        if (isSoft) {
            actor.attemptsTo(
                    GetDetailCategoryTask.withCategoryId(categoryId),
                    Ensure.that("Verify status category: ", BodyResponse.bodyResponse("data.status").asString()).isEqualTo("INACTIVE")
            );
        } else {
            actor.attemptsTo(
                    GetDetailCategoryTask.withCategoryId(categoryId),
                    Ensure.that("Verify status code: ", StatusCodeResponse.responseStatus()).isEqualTo(404),
                    Ensure.that("Verify message: ", BodyResponse.bodyResponse("message").asString()).isEqualTo("Category not found: {0}"),
                    Ensure.that("Verify error: ", BodyResponse.bodyResponse("error").asString()).isEqualTo("CATEGORY_NOT_FOUND")
            );
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

    private static Stream<Arguments> provideDeleteCategoryModes() {
        return Stream.of(
                Arguments.of(true),
                Arguments.of(false)
        );
    }
}
