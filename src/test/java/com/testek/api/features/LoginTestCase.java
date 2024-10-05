package com.testek.api.features;

import com.testek.api.models.AccountModel;
import com.testek.api.questions.BodyResponse;
import com.testek.api.questions.StatusCodeResponse;
import com.testek.api.tasks.LoginTask;
import com.testek.api.utilities.CategoryEndpoints;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;
@ExtendWith(SerenityJUnit5Extension.class)
public class LoginTestCase {
    private static Actor actor;

    @BeforeAll
    static void setUp() {
        actor = Actor.named("tuanTester").whoCan(CallAnApi.at(CategoryEndpoints.BASIC_URL));
    }

    @ParameterizedTest
    @MethodSource("DataLogin")
    void LoginNotSuccess(AccountModel accountModel, String message, Integer statusCode, String responseMess) {
        actor.attemptsTo(
                LoginTask.withAccount(accountModel),
                Ensure.that(message, StatusCodeResponse.responseStatus()).isEqualTo(statusCode),
                Ensure.that(message, BodyResponse.bodyResponse("message").asString()).isEqualTo(responseMess)
        );
    }

    @Test
    void LoginSuccess() {
        actor.attemptsTo(
                LoginTask.withAccount(new AccountModel("testek", "admin")),
                Ensure.that("Login Success", BodyResponse.bodyResponse("access_token").asString()).isNotEmpty()
        );
    }

    private static Stream<Arguments> DataLogin() {
        String mess1 = "Đăng nhập thất bại. Tên đăng nhập hoặc mật khẩu không chính xác";
        return Stream.of(
                Arguments.of(new AccountModel("testek1", "admin"), "Login sai username", 401, mess1),
                Arguments.of(new AccountModel("testek", "admin1"), "Login sai password", 401, mess1),
                Arguments.of(new AccountModel("testek1", "admin1"), "Login sai tài khoản và mật khẩu", 401, mess1),
                Arguments.of(new AccountModel(" testek ", " admin1 "), "Login username và password có khoảng trắng đầu cuối", 401, mess1),
                Arguments.of(new AccountModel(null, "admin1"), "Login không có username", 400, "Dữ liệu nhập không hợp lệ"),
                Arguments.of(new AccountModel(" testek ", null), "Login không có password", 400, "Dữ liệu nhập không hợp lệ")
        );
    }

}
