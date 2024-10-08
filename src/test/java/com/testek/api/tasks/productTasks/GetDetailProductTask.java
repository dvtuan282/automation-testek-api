package com.testek.api.tasks.productTasks;

import com.testek.api.utilities.Endpoints;
import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

public class GetDetailProductTask implements Task {
    private final String productId;

    public GetDetailProductTask(String productId) {
        this.productId = productId;
    }


    @Override
    public <T extends Actor> void performAs(T t) {
        String access_token = "Bearer " + t.recall("access_token");
        t.attemptsTo(
                Get.resource(Endpoints.PRODUCT_GET_BY_ID).with(
                        req -> {
                            req.contentType(ContentType.JSON);
                            req.header("Authorization", access_token);
                            req.pathParam("productId", productId);
                            req.log().uri();
                            req.then().log().body();
                            req.then().log().status();
                            req.that().log().body();
                            return req;
                        }
                )
        );
    }
    public static GetDetailProductTask withIdProduct(String productId) {
        return new GetDetailProductTask(productId);
    }
}
