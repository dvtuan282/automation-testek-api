package com.testek.api.tasks;

import com.testek.api.utilities.Endpoints;
import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Delete;

public class DeleteCategoryTask implements Task {
    private final String categoryId;
    private final boolean isSoft;

    public DeleteCategoryTask(String categoryId, boolean isSoft) {
        this.categoryId = categoryId;
        this.isSoft = isSoft;
    }

    @Override
    public <T extends Actor> void performAs(T t) {
        String access_token = "Bearer " + t.recall("access_token");
        t.attemptsTo(
                Delete.from(Endpoints.CATEGORY_DELETE).with(
                        req -> {
                            req.contentType(ContentType.JSON);
                            req.header("Authorization", access_token);
                            req.pathParams("categoryId", categoryId);
                            req.queryParam("isSoft", isSoft);
                            req.log().uri();
                            req.then().log().status();
                            req.then().log().body();
                            req.that().log().body();
                            return req;
                        }
                )
        );
    }

    public static DeleteCategoryTask deleteCategory(String categoryId, boolean isSoft) {
        return new DeleteCategoryTask(categoryId, isSoft);
    }
}
