package com.testek.api.tasks.supplierTasks;

import com.testek.api.utilities.Endpoints;
import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

public class GetSupplierTask implements Task {
    private final String supplierId;

    public GetSupplierTask(String supplierId) {
        this.supplierId = supplierId;
    }

    @Override
    public <T extends Actor> void performAs(T t) {
        String access_token = "Bearer " + t.recall("access_token");
        t.attemptsTo(
                Get.resource(Endpoints.SUPPLER_GET_BY_ID).with(
                        req -> {
                            req.contentType(ContentType.JSON);
                            req.header("Authorization", access_token);
                            req.pathParam("supplierId", supplierId);
                            req.log().uri();
                            req.then().log().body();
                            req.then().log().status();
                            req.that().log().body();
                            return req;
                        }
                )
        );
    }

    public static GetSupplierTask withSupplierId(String supplierId) {
        return new GetSupplierTask(supplierId);
    }
}
