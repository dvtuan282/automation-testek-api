package com.testek.api.features.categoryFeatures;

import com.testek.api.models.AccountModel;
import com.testek.api.models.CategoryModel;
import com.testek.api.questions.BodyResponse;
import com.testek.api.questions.CategoryResponse;
import com.testek.api.questions.StatusCodeResponse;
import com.testek.api.tasks.*;
import com.testek.api.tasks.CategoryTasks.CreateCategoryTask;
import com.testek.api.tasks.CategoryTasks.DeleteCategoryTask;
import com.testek.api.tasks.CategoryTasks.GetDetailCategoryTask;
import com.testek.api.tasks.CategoryTasks.UpdateCategoryTask;
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
public class UpdateCategoryTestCase {
    private static Actor actor;
    private String categoryId;
    private CategoryModel categoryCreate;

    @BeforeAll
    static void setUp() {
        actor = Actor.named("tuanTester").whoCan(CallAnApi.at(Endpoints.BASIC_URL));
    }

    @BeforeEach
    void login() {
        actor.attemptsTo(
                LoginTask.withAccount(new AccountModel("testek", "admin"))
        );
        // create category
        categoryCreate = new CategoryModel("Update category testcase", "UDCT001", "ACTIVE");
        actor.attemptsTo(
                CreateCategoryTask.withCategory(categoryCreate)
        );
        // lấy path từ body đã tạo thành công
        categoryId = actor.asksFor(BodyResponse.bodyResponse("data.id")).toString();
    }

    @ParameterizedTest
    @MethodSource("provideCategorySuccess")
    void updateCategorySuccess(CategoryModel categoryRequest) {
        actor.attemptsTo(
                UpdateCategoryTask.withData(categoryId, categoryRequest),
                Ensure.that("verify status code: ", StatusCodeResponse.responseStatus()).isEqualTo(200)
        );
        if (categoryRequest.getCateDesc() == null) {
            categoryRequest.setCateDesc(categoryCreate.getCateDesc());
        }
        if (categoryRequest.getCategoryName() == null) {
            categoryRequest.setCategoryName(categoryCreate.getCategoryName());

        }
        actor.attemptsTo(
                GetDetailCategoryTask.withCategoryId(categoryId),
                Ensure.that("Verify category response body: ", CategoryResponse.responseBody(categoryRequest)).isEqualTo("CateRes match CateReq")
        );
    }

    @ParameterizedTest
    @MethodSource("provideCategoryNotSuccess")
    void updateCategoryNotSuccess(CategoryModel categoryRequest, int statusCode) {
        actor.attemptsTo(
                UpdateCategoryTask.withData(categoryId, categoryRequest),
                Ensure.that("verify status code: ", StatusCodeResponse.responseStatus()).isEqualTo(statusCode)
        );
    }

    @Test
    void updateCategoryDuplicateName() {
        CategoryModel categoryRequest = new CategoryModel("testcase create category field name null", "tuanTester3", "ACTIVE");
        actor.attemptsTo(
                UpdateCategoryTask.withData(categoryId, categoryRequest),
                Ensure.that("verify status code: ", StatusCodeResponse.responseStatus()).isEqualTo(400)
        );
    }

    @AfterEach
    public void cleanUp() {
        if (categoryId != null) {
            actor.attemptsTo(DeleteCategoryTask.withDetails(categoryId, false));
        }
    }


    private static Stream<Arguments> provideCategorySuccess() {
        return Stream.of(
                Arguments.of(new CategoryModel("testcase create category success 2 ", "CTT001", "ACTIVE")),
                Arguments.of(new CategoryModel(null, "CTT001", "ACTIVE")),
                Arguments.of(new CategoryModel("testcase create category success 1", "CTT001", "INACTIVE")),
                Arguments.of(new CategoryModel("testcase create category field name null", null, "ACTIVE"), 200)
        );
    }

    private static Stream<Arguments> provideCategoryNotSuccess() {
        return Stream.of(
                Arguments.of(new CategoryModel("testcase create category fiedd name blank", "", "ACTIVE"), 400),
                Arguments.of(new CategoryModel("testcase create category field name space", " ", "INACTIVE"), 400),
                Arguments.of(new CategoryModel("testcase create category field name duplicate", "tuanTester3", "ACTIVE"), 400),
                Arguments.of(new CategoryModel("testcase create category status invalid", "update status invalid", "INACTIVEF"), 400)
        );
    }
}
