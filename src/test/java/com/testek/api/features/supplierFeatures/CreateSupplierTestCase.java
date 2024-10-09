package com.testek.api.features.supplierFeatures;

import com.testek.api.models.AccountModel;
import com.testek.api.models.SupplierModel;
import com.testek.api.questions.BodyResponse;
import com.testek.api.questions.StatusCodeResponse;
import com.testek.api.questions.SupplierQuestion;
import com.testek.api.tasks.LoginTask;
import com.testek.api.tasks.supplierTasks.CreateSupplierTask;
import com.testek.api.tasks.supplierTasks.DeleteSupplierTask;
import com.testek.api.tasks.supplierTasks.GetSupplierTask;
import com.testek.api.utilities.Endpoints;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

@ExtendWith(SerenityJUnit5Extension.class)
public class CreateSupplierTestCase {
    private static Actor actor;
    private String idSupplier;

    @BeforeAll
    static void setup() {
        actor = Actor.named("tuanTester")
                .whoCan(CallAnApi.at(Endpoints.BASIC_URL));
    }

    @BeforeEach
    void Login() {
        actor.attemptsTo(
                LoginTask.withAccount(new AccountModel("testek", "admin"))
        );
    }

    @ParameterizedTest
    @MethodSource("providedSupplierSuccess")
    void CreateSupplierSuccess(SupplierModel supplierInput, String description, Integer statusExpected) {
        actor.attemptsTo(
                CreateSupplierTask.withSupplier(supplierInput),
                Ensure.that(description, StatusCodeResponse.responseStatus().asInteger()).isEqualTo(statusExpected)
        );
        idSupplier = actor.asksFor(BodyResponse.bodyResponse("data.id")).toString();
        actor.attemptsTo(
                GetSupplierTask.withSupplierId(idSupplier),
                Ensure.that(SupplierQuestion.responseSupp(supplierInput)).isEqualTo("supplierRes match supplierReq")
        );
    }

    @ParameterizedTest
    @MethodSource("providedSupplierNotSuccess")
    void CreateSupplierNotSuccess(SupplierModel supplierInput, String description, Integer statusExpected, String errorExpected) {
        actor.attemptsTo(
                CreateSupplierTask.withSupplier(supplierInput),
                Ensure.that(description, StatusCodeResponse.responseStatus().asInteger()).isEqualTo(statusExpected),
                Ensure.that(description, BodyResponse.bodyResponse("error").asString()).isEqualTo(errorExpected)
        );
        int statusCheck = actor.asksFor(StatusCodeResponse.responseStatus().asInteger());
        if (statusCheck != 400) {
            idSupplier = actor.asksFor(BodyResponse.bodyResponse("data.id")).toString();
        }
    }

    @AfterEach
    void clean_data() {
        if (idSupplier != null) {
            actor.attemptsTo(
                    DeleteSupplierTask.withSuppliers(idSupplier)
            );
        }
    }

    private static Stream<Arguments> providedSupplierSuccess() {
        return Stream.of(
                Arguments.of(new SupplierModel("Phổ Yên", "Thái Nguyên", "Cty A", "Việt Nam", "0987654321", "5435", "Công ty b123234"),
                        "Create supplier success with data valid", 201)
        );
    }

    private static Stream<Arguments> providedSupplierNotSuccess() {
        return Stream.of(
                Arguments.of(new SupplierModel("", "Thái Nguyên", "", "Việt Nam", "", "5435", ""),
                        "Create supplier success when field required not data", 400, "INVALID_INPUT"),
                Arguments.of(new SupplierModel(" ", "Thái Nguyên", " ", "Việt Nam", " ", "5435", " "),
                        "Create supplier success when field required is blank", 400, "INVALID_INPUT"),
                Arguments.of(new SupplierModel("Phổ Yên", "Thái Nguyên", "Cty A", "Việt Nam", "987654321", "5435", "Công ty c2"),
                        "Create supplier not success when field Phone numbers do not start with 0 ", 400, "INVALID_INPUT"),
                Arguments.of(new SupplierModel("Phổ Yên", "Thái Nguyên", "Cty A", "Việt Nam", "987654321", "5435", "Công ty b3"),
                        "Create supplier not success when The length of the phone number is less than 10 ", 400, "INVALID_INPUT"),
                Arguments.of(new SupplierModel("Phổ Yên", "Thái Nguyên", "Cty A", "Việt Nam", "987654321", "5435", "Công ty b4"),
                        "Create supplier not success when Phone numbers are not numbers ", 400, "INVALID_INPUT"),
                Arguments.of(new SupplierModel("Phổ Yên", "Thái Nguyên", "Cty A", "Việt Nam", "987654321", "5435", "name1"),
                        "Create supplier not success when name is existed", 400, "SUPPLIER_EXISTED")
        );
    }

}
