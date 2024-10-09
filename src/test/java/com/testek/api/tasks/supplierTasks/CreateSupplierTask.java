package com.testek.api.tasks.supplierTasks;

import com.testek.api.models.SupplierModel;
import com.testek.api.utilities.Endpoints;
import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

public class CreateSupplierTask implements Task {
    private final SupplierModel supplierModel;

    public CreateSupplierTask(SupplierModel supplierModel) {
        this.supplierModel = supplierModel;
    }

    @Override
    public <T extends Actor> void performAs(T t) {
        String access_token = "Bearer " + t.recall("access_token");
        t.attemptsTo(
                Post.to(Endpoints.SUPPLER_CREATE).with(
                        req -> {
                            req.contentType(ContentType.JSON);
                            req.body(supplierModel);
                            req.header("Authorization", access_token);
                            req.log().uri();
                            req.then().log().body();
                            req.then().log().status();
                            req.that().log().body();
                            return req;
                        }
                )
        );
    }
    public static CreateSupplierTask withSupplier(SupplierModel supplierModel) {
        return new CreateSupplierTask(supplierModel);
    }
}
