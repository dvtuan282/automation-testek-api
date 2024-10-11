package com.testek.api.questions;

import com.testek.api.models.CategoryModel;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class CategoryResponse implements Question<String> {
    private final CategoryModel categoryRequest;

    public CategoryResponse(CategoryModel categoryRequest) {
        this.categoryRequest = categoryRequest;
    }

    @Override
    public String answeredBy(Actor actor) {
        // Lấy phản hồi cuối cùng từ API
        CategoryModel categoryResponseBody = SerenityRest.lastResponse().jsonPath().getObject("data", CategoryModel.class);
        CategoryModel categoryResponse = new CategoryModel(
                categoryResponseBody.getCateDesc(),
                categoryResponseBody.getCategoryName(),
                categoryResponseBody.getStatus()
        );
        System.out.println("Category Request: " + categoryRequest);
        System.out.println("Category Response: " + categoryResponse);

        if (categoryResponse.equals(categoryRequest)) {
            return "CateRes match CateReq";
        } else {
            return "CateRes not match CateReq";
        }
    }

    public static CategoryResponse responseBody(CategoryModel categoryReq) {
        return new CategoryResponse(categoryReq);
    }
}
