package com.testek.api.tasks;

import com.testek.api.models.CategoryModel;
import com.testek.api.utilities.CategoryEndpoints;
import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

public class CreateCategoryTask implements Task {
    private final CategoryModel categoryModel;

    public CreateCategoryTask(CategoryModel categoryModel) {
        this.categoryModel = categoryModel;
    }

    @Override
    public <T extends Actor> void performAs(T t) {
        String access_token = "Bearer " + t.recall("access_token");
        t.attemptsTo(
                Post.to(CategoryEndpoints.CATEGORY_CREATE).with(
                        req -> {
                            req.contentType(ContentType.JSON);
                            req.body(categoryModel);
                            req.header("Authorization", access_token);
                            req.log().uri();
                            req.then().log().all();
                            req.log().body();
                            return req;
                        }
                )
        );
    }

    public static CreateCategoryTask withCategory(CategoryModel categoryModel) {
        return new CreateCategoryTask(categoryModel);
    }

}
