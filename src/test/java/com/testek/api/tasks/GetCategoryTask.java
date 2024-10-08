package com.testek.api.tasks;

import com.testek.api.utilities.Endpoints;
import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;


public class GetCategoryTask implements Task {
    private final String categoryId;

    public GetCategoryTask(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public <T extends Actor> void performAs(T t) {
        String access_token = "Bearer " + t.recall("access_token");
        t.attemptsTo(
                Get.resource(Endpoints.CATEGORY_GET_BY_ID).with(
                        req -> {
                            req.contentType(ContentType.JSON);
                            req.pathParams("categoryId", categoryId);
                            req.header("Authorization", access_token);
                            req.log().uri();
                            req.then().log().status();
                            req.then().log().body();
                            req.that().log().body();
                            return req;
                        }
                )
        );
    }

    public static GetCategoryTask withCategoryId(String categoryId) {
        return new GetCategoryTask(categoryId);
    }
}
