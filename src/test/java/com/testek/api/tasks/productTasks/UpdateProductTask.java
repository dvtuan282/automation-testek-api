package com.testek.api.tasks.productTasks;

import com.testek.api.models.ProductModel;
import com.testek.api.utilities.Endpoints;
import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Put;

public class UpdateProductTask implements Task {
    private final String productId;
    private final ProductModel productRequest;

    public UpdateProductTask(String productId, ProductModel productRequest) {
        this.productId = productId;
        this.productRequest = productRequest;
    }

    @Override
    public <T extends Actor> void performAs(T t) {
        String access_token = "Bearer " + t.recall("access_token");
        t.attemptsTo(
                Put.to(Endpoints.PRODUCT_UPDATE).with(
                        req -> {
                            req.contentType(ContentType.JSON);
                            req.header("Authorization", access_token);
                            req.pathParam("productId", productId);
                            req.body(productRequest);
                            req.log().uri();
                            req.then().log().body();
                            req.then().log().status();
                            req.that().log().body();
                            return req;
                        }
                )
        );
    }

    public static UpdateProductTask withProduct(String productId, ProductModel productRequest) {
        return new UpdateProductTask(productId, productRequest);
    }
}
