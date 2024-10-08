package com.testek.api.tasks.supplierTasks;

import com.testek.api.models.SupplierModel;
import com.testek.api.utilities.Endpoints;
import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Put;

public class UpdateSupplierTask implements Task {
    private final String supplierId;
    private final SupplierModel supplierModel;

    public UpdateSupplierTask(String supplierId, SupplierModel supplierModel) {
        this.supplierId = supplierId;
        this.supplierModel = supplierModel;
    }

    @Override
    public <T extends Actor> void performAs(T t) {
        String access_token = "Bearer " + t.recall("access_token");
        t.attemptsTo(
                Put.to(Endpoints.SUPPLER_UPDATE).with(
                        req -> {
                            req.contentType(ContentType.JSON);
                            req.header("authorization", access_token);
                            req.pathParam("id", supplierId);
                            req.body(supplierModel);
                            req.log().uri();
                            req.then().log().body();
                            req.then().log().status();
                            req.that().log().body();
                            return req;
                        }
                )
        );
    }

    public static UpdateSupplierTask withSupplier(String supplierId, SupplierModel supplierModel) {
        return new UpdateSupplierTask(supplierId, supplierModel);
    }
}
