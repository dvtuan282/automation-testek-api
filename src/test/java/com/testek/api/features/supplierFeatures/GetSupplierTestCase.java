package com.testek.api.features.supplierFeatures;

import com.testek.api.models.AccountModel;
import com.testek.api.models.SupplierModel;
import com.testek.api.questions.BodyResponse;
import com.testek.api.questions.StatusCodeResponse;
import com.testek.api.tasks.LoginTask;
import com.testek.api.tasks.supplierTasks.CreateSupplier;
import com.testek.api.tasks.supplierTasks.DeleteSupplierTask;
import com.testek.api.tasks.supplierTasks.GetSupplierTask;
import com.testek.api.utilities.CategoryEndpoints;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SerenityJUnit5Extension.class)
public class GetSupplierTestCase {
    private static Actor actor;
    private String idSupplier;

    @BeforeAll
    static void setup() {
        actor = Actor.named("tuanTester").whoCan(CallAnApi.at(CategoryEndpoints.BASIC_URL));
    }


    @BeforeEach
    void LoginAndCreate() {
        actor.attemptsTo(
                LoginTask.withAccount(new AccountModel("Testek", "admin"))
        );

        actor.attemptsTo(
                CreateSupplier.withSupplier(new SupplierModel("Phổ Yên", "Thái Nguyên", "Cty A", "Việt Nam", "0987654321", "5435", "Công ty b1"))
        );
        idSupplier = actor.asksFor(BodyResponse.bodyResponse("data.id")).toString();
    }

    @Test
    void GetDetailSupplierSuccess() {
        actor.attemptsTo(
                GetSupplierTask.withSupplierId(idSupplier),
                Ensure.that("Status code is 200 when get details supplier success", StatusCodeResponse.responseStatus()).isEqualTo(200),
                Ensure.that("Id supplier equals id supplier response", BodyResponse.bodyResponse("data.id").asString()).isEqualTo(idSupplier)
        );
    }
    @Test
    void GetDetailSupplierNotSuccess() {
        actor.attemptsTo(
                GetSupplierTask.withSupplierId(idSupplier),
                Ensure.that("Status code is 200 when get details supplier success", StatusCodeResponse.responseStatus()).isEqualTo(400),
                Ensure.that("Id supplier equals id supplier response", BodyResponse.bodyResponse("error").asString()).isEqualTo(idSupplier)
        );
    }

    @AfterEach
    void CleanUp() {
        actor.attemptsTo(
                DeleteSupplierTask.withSuppliers(idSupplier)
        );
    }


}
