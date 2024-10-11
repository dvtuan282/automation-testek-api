package com.testek.api.features.supplierFeatures;

import com.testek.api.models.AccountModel;
import com.testek.api.models.SupplierModel;
import com.testek.api.questions.BodyResponse;
import com.testek.api.questions.StatusCodeResponse;
import com.testek.api.questions.SupplierResponse;
import com.testek.api.tasks.LoginTask;
import com.testek.api.tasks.supplierTasks.CreateSupplierTask;
import com.testek.api.tasks.supplierTasks.DeleteSupplierTask;
import com.testek.api.tasks.supplierTasks.GetDetailSupplierTask;
import com.testek.api.tasks.supplierTasks.UpdateSupplierTask;
import com.testek.api.utilities.Endpoints;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

@ExtendWith(SerenityJUnit5Extension.class)
public class UpdateSupplierTestCase {
    private static Actor actor;
    private String idSupplier;


    @BeforeAll
    static void setUp() {
        actor = Actor.named("tuanTester").whoCan(CallAnApi.at(Endpoints.BASIC_URL));
    }

    @BeforeEach
    void loginAndCreateSupplier() {
        actor.attemptsTo(
                LoginTask.withAccount(new AccountModel("testek", "admin"))
        );
        actor.attemptsTo(
                CreateSupplierTask.withSupplier(new SupplierModel("Phổ Yên", "Thái Nguyên", "Cty A", "Việt Nam", "09876543232", "5435", "Công ty up102"))
        );

        int statusResponse = actor.asksFor(StatusCodeResponse.responseStatus());
        if (statusResponse == 201 || statusResponse == 200) {
            idSupplier = actor.asksFor(BodyResponse.bodyResponse("data.id")).toString();
        }
    }

    @ParameterizedTest
    @MethodSource("providedSupplierSuccess")
    void updateSupplierSuccess(SupplierModel supplierRequest, String description, int statusCodeExpected) {
        actor.attemptsTo(
                UpdateSupplierTask.withSupplier(idSupplier, supplierRequest),
                Ensure.that(description, StatusCodeResponse.responseStatus()).isEqualTo(statusCodeExpected)
        );
        actor.attemptsTo(
                GetDetailSupplierTask.withSupplierId(idSupplier),
                Ensure.that(SupplierResponse.responseSupp(supplierRequest)).isEqualTo("supplierRes match supplierReq")
        );
    }

    @ParameterizedTest
    @MethodSource("providedSupplierNotSuccess")
    void updateSupplierNotSuccess(SupplierModel supplierRequest, int statusCodeExpected) {
        actor.attemptsTo(
                UpdateSupplierTask.withSupplier(idSupplier, supplierRequest)
        );
        int statusActual = actor.asksFor(StatusCodeResponse.responseStatus());
        actor.attemptsTo(
                Ensure.that(statusActual).isEqualTo(statusCodeExpected)
        );
    }

    @Test
    void updateSupplierWhenIdIsWrong() {
        String idIsWrong = "a3400b55-b187-4fc2-b25b-81b84";
        SupplierModel supplierRequest = new SupplierModel("Phổ Yên1", "Thái Nguyên2", "Cty A3", "Việt Nam4", "09876543215", "54356", "Công ty b17");
        actor.attemptsTo(
                UpdateSupplierTask.withSupplier(idIsWrong, supplierRequest),
                Ensure.that("Update supplier when id does not exist", StatusCodeResponse.responseStatus()).isEqualTo(404),
                Ensure.that(BodyResponse.bodyResponse("error").asString()).isEqualTo("SUPPLIER_NOT_FOUND")
        );

    }
    @AfterEach
    public void cleanUp() {
        System.out.println("aaa");
        actor.attemptsTo(
                DeleteSupplierTask.withSuppliers(idSupplier)
        );
        System.out.println("bbb");
    }

    public static Stream<Arguments> providedSupplierSuccess() {
        return Stream.of(
                Arguments.of(
                        new SupplierModel("Phổ Yên1", "Thái Nguyên2", "Cty A3", "Việt Nam4", "09876543215", "54356", "Công ty b17"),
                        "Update supplier with data valid", 200),
                Arguments.of(
                        new SupplierModel("Phổ Yên1", null, "Cty A3", null, "09876543215", null, "Công ty b17"),
                        "Update supplier when field option is null", 200),
                Arguments.of(
                        new SupplierModel("Phổ Yên1", " ", "Cty A3", " ", "09876543215", " ", "Công ty b17"),
                        "Update supplier when field option is blank", 200)
        );
    }

    public static Stream<Arguments> providedSupplierNotSuccess() {
        return Stream.of(
                Arguments.of(
                        new SupplierModel(" ", "Thái Nguyên2", " ", "Việt Nam4", " ", "54356", " "), 400),
                Arguments.of(
                        new SupplierModel("", "Thái Nguyên2", "", "Việt Nam4", "", "54356", ""),
                        400),
                Arguments.of(
                        new SupplierModel("Phổ Yến 12", "Thái Nguyên2", "ABV", "Việt Nam4", "098754324", "54356", "Testek"),
                        400),
                Arguments.of(
                        new SupplierModel("Phổ Cập3", "Thái Nguyên2", "T12", "Việt Nam4", "098765", "54356", "Tester"),
                        400),
                Arguments.of(
                        new SupplierModel("Phổ Cập3", "Thái Nguyên2", "T12", "Việt Nam4", "09876543211", "54356", "Tester"),
                        400),
                Arguments.of(
                        new SupplierModel("Phổ Cập3", "Thái Nguyên2", "T12", "Việt Nam4", "09876543qw", "54356", "Tester"),
                        400),
                Arguments.of(
                        new SupplierModel("Phổ Cập3", "Thái Nguyên2", "T12", "Việt Nam4", "9876543211", "54356", "Tester"),
                        400)
        );
    }

}
