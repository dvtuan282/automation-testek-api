package com.testek.api.tasks.productTasks;

import com.testek.api.models.ProductModel;
import com.testek.api.utilities.Endpoints;
import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.util.Map;

public class CreateProductTask implements Task {
    private final ProductModel productRequest;

    public CreateProductTask(ProductModel productRequest) {
        this.productRequest = productRequest;
    }

    @Override
    public <T extends Actor> void performAs(T t) {
        String access_token = "Bearer " + t.recall("access_token");
        t.attemptsTo(
                Post.to(Endpoints.PRODUCT_CREATE).with(
                        req -> {
                            req.contentType(ContentType.JSON);
                            req.header("Authorization", access_token);
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

    public static CreateProductTask withProduct(ProductModel productRequest) {
        return new CreateProductTask(productRequest);
    }
}
