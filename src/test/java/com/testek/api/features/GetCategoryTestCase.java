package com.testek.api.features;

import com.testek.api.models.AccountModel;
import com.testek.api.questions.BodyResponse;
import com.testek.api.questions.StatusCodeResponse;
import com.testek.api.tasks.GetCategoryTask;
import com.testek.api.tasks.LoginTask;
import com.testek.api.utilities.Endpoints;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SerenityJUnit5Extension.class)
public class GetCategoryTestCase {
    private static Actor actor;

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

    @Test
    void getACategorySuccess() {
        String categoryId = "74374d66-a270-4cd1-ae58-8b1b4265a687";
        actor.attemptsTo(
                GetCategoryTask.withCategoryId(categoryId),
                Ensure.that("Successfully get categoty by id with status is 200", StatusCodeResponse.responseStatus()).isEqualTo(200),
                Ensure.that(BodyResponse.bodyResponse("data.id").asString()).isEqualTo(categoryId)
        );
        System.out.println("a: " + BodyResponse.bodyResponse("data"));
    }

    @Test
    void getACategoryNotFound() {
        String categoryId = "344128b6-75c6-40e0-8a14-78c8692a38e2";
        String errorExpected = "CATEGORY_NOT_FOUND";
        actor.attemptsTo(
                GetCategoryTask.withCategoryId(categoryId),
                Ensure.that("Successfully get categoty by id with status is 200", StatusCodeResponse.responseStatus()).isEqualTo(404),
                Ensure.that(BodyResponse.bodyResponse("error").asString()).isEqualTo(errorExpected)
        );
    }
}
