package com.testek.api.features.productFeatures;

import com.testek.api.models.AccountModel;
import com.testek.api.models.ProductModel;
import com.testek.api.questions.BodyResponse;
import com.testek.api.questions.StatusCodeResponse;
import com.testek.api.tasks.LoginTask;
import com.testek.api.tasks.productTasks.CreateProductTask;
import com.testek.api.tasks.productTasks.GetDetailProductTask;
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
public class GetProductDetailTestCase {
    private static Actor actor;

    @BeforeAll
    static void setup() {
        actor = Actor.named("tuanTester")
                .whoCan(CallAnApi.at(Endpoints.BASIC_URL));
    }

    @BeforeEach
    void login() {
        actor.attemptsTo(
                LoginTask.withAccount(new AccountModel("testek", "admin"))
        );

    }

    @Test
    void getProductDetailSuccess() {
        String idProduct = "82e4db18-3fd4-4959-8c59-2dc4f00ffa57";
        actor.attemptsTo(
                GetDetailProductTask.withIdProduct(idProduct),
                Ensure.that(StatusCodeResponse.responseStatus()).isEqualTo(200),
                Ensure.that(BodyResponse.bodyResponse("data.id").asString()).isEqualTo(idProduct)
        );
    }

    @Test
    void getProductDetailNotFound() {
        String idProduct = "82e4db18-3fd4-4959-8c59-2dc4f";
        actor.attemptsTo(
                GetDetailProductTask.withIdProduct(idProduct),
                Ensure.that(StatusCodeResponse.responseStatus()).isEqualTo(404),
                Ensure.that(BodyResponse.bodyResponse("error").asString()).isEqualTo("PRODUCT_NOT_FOUND")
        );
    }
}
