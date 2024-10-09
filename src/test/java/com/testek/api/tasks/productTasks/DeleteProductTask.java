package com.testek.api.tasks.productTasks;

import com.testek.api.utilities.Endpoints;
import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Delete;

public class DeleteProductTask implements Task {
    private final String productId;
    private final boolean isSoft;

    public DeleteProductTask(String productId, boolean isSoft) {
        this.productId = productId;
        this.isSoft = isSoft;
    }

    @Override
    public <T extends Actor> void performAs(T t) {
        String access_token = "Bearer " + t.recall("access_token");

        t.attemptsTo(
                Delete.from(Endpoints.PRODUCT_DELETE).with(
                        req -> {
                            req.contentType(ContentType.JSON);
                            req.header("Authorization", access_token);
                            req.pathParam("productId", productId);
                            req.queryParam("isSoft", isSoft);
                            req.log().uri();
                            req.then().log().body();
                            req.then().log().status();
                            req.that().log().body();
                            return req;
                        }
                )
        );
    }

    public static DeleteProductTask withProductId(String productId, boolean isSoft) {
        return new DeleteProductTask(productId, isSoft);
    }
}
