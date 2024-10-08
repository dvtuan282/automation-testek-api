package com.testek.api.tasks.supplierTasks;

import com.testek.api.utilities.Endpoints;
import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Delete;

public class DeleteSupplierTask implements Task {
    private final String supplierId;

    public DeleteSupplierTask(String supplierId) {
        this.supplierId = supplierId;
    }

    @Override
    public <T extends Actor> void performAs(T t) {
        String access_token = "Bearer " + t.recall("access_token");

        t.attemptsTo(
                Delete.from(Endpoints.SUPPLER_DELETE).with(
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

    public static DeleteSupplierTask withSuppliers(String supplierId) {
        return new DeleteSupplierTask(supplierId);
    }


}
