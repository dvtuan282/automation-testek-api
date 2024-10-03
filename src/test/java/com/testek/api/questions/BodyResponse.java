package com.testek.api.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class BodyResponse implements Question<Object> {

    private final String field;

    public BodyResponse(String field) {
        this.field = field;
    }
    @Override
    public Object answeredBy(Actor actor) {
        return SerenityRest.lastResponse().jsonPath().get(field);
    }
    public static BodyResponse bodyResponse(String field){
        return new BodyResponse(field);
    }
}
