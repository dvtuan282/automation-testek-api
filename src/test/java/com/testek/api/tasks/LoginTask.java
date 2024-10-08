package com.testek.api.tasks;

import com.testek.api.models.AccountModel;
import com.testek.api.utilities.Endpoints;
import io.restassured.http.ContentType;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

public class LoginTask implements Task {
    private AccountModel accountModel;

    public LoginTask(AccountModel accountModel) {
        this.accountModel = accountModel;
    }

    public static LoginTask withAccount(AccountModel accountModel) {
        return new LoginTask(accountModel);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Post.to(Endpoints.LOGIN).with(
                        req -> {
                            req.contentType(ContentType.JSON);
                            req.body(accountModel);
                            req.log().uri();
                            req.then().log().all();
                            req.log().body();
                            return req;
                        }
                )
        );
        String access_token = SerenityRest.lastResponse().jsonPath().get("access_token");
        System.out.println("get token in response");
        actor.remember("access_token", access_token);
    }
}
