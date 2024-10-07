package com.testek.api.tasks;

import com.testek.api.models.CategoryModel;
import com.testek.api.utilities.CategoryEndpoints;
import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Put;

public class UpdateCategoryTask implements Task {
    private final String categoryId;
    private final CategoryModel categoryModel;

    public UpdateCategoryTask(String categoryId, CategoryModel categoryModel) {
        this.categoryId = categoryId;
        this.categoryModel = categoryModel;
    }

    @Override
    public <T extends Actor> void performAs(T t) {
        String access_token = "Bearer " + t.recall("access_token");
        t.attemptsTo(
                Put.to(CategoryEndpoints.CATEGORY_UPDATE).with(
                        req -> {
                            req.contentType(ContentType.JSON);
                            req.header("Authorization", access_token);
                            req.pathParam("id", categoryId);
                            req.body(categoryModel);
                            req.log().uri();
                            req.then().log().status();
                            req.then().log().body();
                            req.that().log().body();
                            return req;
                        }
                )
        );
    }

    public static UpdateCategoryTask updateCategoryWith(String categoryId, CategoryModel categoryModel) {
        return new UpdateCategoryTask(categoryId, categoryModel);
    }
}
