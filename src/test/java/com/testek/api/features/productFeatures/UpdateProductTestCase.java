package com.testek.api.features.productFeatures;

import com.testek.api.models.AccountModel;
import com.testek.api.models.ProductModel;
import com.testek.api.questions.BodyResponse;
import com.testek.api.questions.StatusCodeResponse;
import com.testek.api.tasks.LoginTask;
import com.testek.api.tasks.productTasks.CreateProductTask;
import com.testek.api.tasks.productTasks.DeleteProductTask;
import com.testek.api.tasks.productTasks.UpdateProductTask;

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

import java.util.stream.Stream;
@ExtendWith(SerenityJUnit5Extension.class)
public class UpdateProductTestCase {
    private static Actor actor;
    private String productId;

    @BeforeAll
    static void setup() {
        actor = Actor.named("tuanTester")
                .whoCan(CallAnApi.at(Endpoints.BASIC_URL));
    }
    @BeforeEach
    public void loginAndCreate() {
        ProductModel productModel = new ProductModel(
                "74374d66-a270-4cd1-ae58-8b1b4265a687",
                "da7c9b54-5d85-4c49-a8d7-3e2f737168fc",
                "Sản phẩm tạo ra để test update project",
                "Sản phẩm testUpdate190",
                20.0,
                34,
                "kg",
                "pud0190"
        );
        actor.attemptsTo(
                LoginTask.withAccount(new AccountModel("testek", "admin"))
        );
        actor.attemptsTo(
                CreateProductTask.withProduct(productModel)
        );
        int statusResponseProduct = actor.asksFor(StatusCodeResponse.responseStatus());
        if (statusResponseProduct == 201 || statusResponseProduct == 200) {
            productId = actor.asksFor(BodyResponse.bodyResponse("data.id")).toString();
        }
    }

    //    @ParameterizedTest
//    @MethodSource("provideProductSuccess")
    @Test
    void updateProjectSuccess() {
        ProductModel productRequest = new ProductModel(
                "130c930a-62cf-4d9e-b6e2-19b8645b00a8",
                "6a0895ef-2b58-473e-9278-50e9aac7df87",
                "Update success 1",
                "update name success 1",
                200.0,
                30,
                "kg", "code103");

        actor.attemptsTo(
                UpdateProductTask.withProduct(productId, productRequest),
                Ensure.that(StatusCodeResponse.responseStatus()).isEqualTo(400)
        );
    }

    @AfterEach
    public void cleanUp() {
        System.out.println("Start clean");
        if (productId != null) {
            System.out.println("a1");
            actor.attemptsTo(
                    DeleteProductTask.withProductId(productId, false),
                    Ensure.that(StatusCodeResponse.responseStatus()).isEqualTo(200)
            );
        }
        System.out.println("Clean success");
    }
}
