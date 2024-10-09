package com.testek.api.features.projectFeatures;

import com.testek.api.models.AccountModel;
import com.testek.api.models.ProductModel;
import com.testek.api.questions.BodyResponse;
import com.testek.api.questions.StatusCodeResponse;
import com.testek.api.tasks.LoginTask;
import com.testek.api.tasks.productTasks.CreateProductTask;
import com.testek.api.tasks.productTasks.DeleteProductTask;
import com.testek.api.utilities.Endpoints;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

@ExtendWith(SerenityJUnit5Extension.class)
public class CreateProductTestCase {
    private static Actor actor;
    private String productId;

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

    @ParameterizedTest
    @MethodSource("provideProductSuccess")
    void createProductSuccess(ProductModel productRequest) {
        actor.attemptsTo(
                CreateProductTask.withProduct(productRequest),
                Ensure.that(StatusCodeResponse.responseStatus()).isEqualTo(201)
        );
        int statusResponse = actor.asksFor(StatusCodeResponse.responseStatus());
        if (statusResponse == 201) {
            productId = actor.asksFor(BodyResponse.bodyResponse("data.id")).toString();
        }
    }
    @ParameterizedTest
    @MethodSource("provideProductNotSuccess")
    void createProductNotSuccess(ProductModel productRequest, Integer statusExpected) {
        actor.attemptsTo(
                CreateProductTask.withProduct(productRequest),
                Ensure.that(StatusCodeResponse.responseStatus()).isEqualTo(statusExpected)
        );
        int statusResponse = actor.asksFor(StatusCodeResponse.responseStatus());
        if (statusResponse == 201) {
            productId = actor.asksFor(BodyResponse.bodyResponse("data.id")).toString();
        }
    }

    @AfterEach
    void cleanUp() {
        System.out.println("id " + productId);
        if (productId != null) {
            actor.attemptsTo(
                    DeleteProductTask.withProductId(productId,false)
            );
        }
    }

    static Stream<Arguments> provideProductSuccess() {
        return Stream.of(
                Arguments.of(new ProductModel("74374d66-a270-4cd1-ae58-8b1b4265a687", "a3400b55-b187-4fc2-b25b-81ba21241938", "Đây là sản phẩm gửi đến Liên", "iphone 16", 20000.0, 30, "IP167", "code103")),
                Arguments.of(new ProductModel("74374d66-a270-4cd1-ae58-8b1b4265a687","a3400b55-b187-4fc2-b25b-81ba21241938", null, "iphone 16", 20000.0, 30, null, "code103"))
        );
    }

    static Stream<Arguments> provideProductNotSuccess(){
        return Stream.of(
                Arguments.of(new ProductModel("74374d66-a270-4cd1-ae58-8b1b4265a699", "a3400b55-b187-4fc2-b25b-81ba21241938", "Đây là sản phẩm gửi đến Liên", "iphone 16", 20000.0, 30, "IP167", "code020"), 404),
                Arguments.of(new ProductModel("74374d66-a270-4cd1-ae58-8b1b4265a687", "a3400b55-b187-4fc2-b25b-81ba21241900", "Đây là sản phẩm gửi đến Liên", "iphone 16", 20000.0, 30, "IP167", "code021"), 404),
                Arguments.of(new ProductModel("74374d66-a270", "a3400b55-b187-4fc2-b25b-81ba21241938", "Đây là sản phẩm gửi đến Liên", "iphone 16", 20000.0, 30, "IP167", "code022"), 400),
                Arguments.of(new ProductModel("74374d66-a270-4cd1-ae58-8b1b4265a687", "a3400b55-b187-", "Đây là sản phẩm gửi đến Liên", "iphone 16", 20000.0, 30, "IP167", "code023"), 400),
                Arguments.of(new ProductModel("74374d66-a270-4cd1-ae58-8b1b4265a687", "a3400b55-b187-4fc2-b25b-81ba21241938", "Đây là sản phẩm gửi đến Liên", "iphone 16", 20000.0, 30, "IP167", "0024"), 400),
                Arguments.of(new ProductModel("74374d66-a270-4cd1-ae58-8b1b4265a687", "a3400b55-b187-4fc2-b25b-81ba21241938", "Đây là sản phẩm gửi đến Liên", "iphone 16", -98.0, 30, "IP167", "code025"), 400),
                Arguments.of(new ProductModel("74374d66-a270-4cd1-ae58-8b1b4265a687", "a3400b55-b187-4fc2-b25b-81ba21241938", "Đây là sản phẩm gửi đến Liên", "iphone 16", 0.123, 30, "IP167", "code026"), 400),
                Arguments.of(new ProductModel("74374d66-a270-4cd1-ae58-8b1b4265a687", "a3400b55-b187-4fc2-b25b-81ba21241938", "Đây là sản phẩm gửi đến Liên", "iphone 16", 20000.0, -30, "IP167", "code028"), 400),
                Arguments.of(new ProductModel("74374d66-a270-4cd1-ae58-8b1b4265a687", "a3400b55-b187-4fc2-b25b-81ba21241938", "Đây là sản phẩm gửi đến Liên", "", null, null, "IP167", ""), 400),
                Arguments.of(new ProductModel("74374d66-a270-4cd1-ae58-8b1b4265a687", "a3400b55-b187-4fc2-b25b-81ba21241938", "Đây là sản phẩm gửi đến Liên", " ", null, null, "IP167", " "), 400),
                Arguments.of(new ProductModel("74374d66-a270-4cd1-ae58-8b1b4265a687", "a3400b55-b187-4fc2-b25b-81ba21241938", "Đây là sản phẩm gửi đến Liên", null, 20000.0, null, "IP167", null), 400)
        );
    }
}
