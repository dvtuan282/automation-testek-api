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
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SerenityJUnit5Extension.class)
public class CreateSupplierTestCase {
    private static Actor actor;
    private String idSupplier;

    @BeforeAll
    static void setup() {
        actor = Actor.named("tuanTester")
                .whoCan(CallAnApi.at(CategoryEndpoints.BASIC_URL));
    }

    @BeforeEach
    void Login() {
        actor.attemptsTo(
                LoginTask.withAccount(new AccountModel("testek", "admin"))
        );
    }

    void CreateSupplierSuccess(SupplierModel supplierInput, String description, Integer statusExpected) {
        actor.attemptsTo(
                CreateSupplier.withSupplier(supplierInput),
                Ensure.that(description, StatusCodeResponse.responseStatus().asInteger()).isEqualTo(statusExpected)
        );
        idSupplier = actor.asksFor(BodyResponse.bodyResponse("sdd")).toString();

        actor.attemptsTo(
                GetSupplierTask.withSupplierId(idSupplier)
//                Ensure.that(BodyResponse.bodyResponse("as").as(Object.class))
        );
    }

    @AfterEach
    void clean_data() {
        actor.attemptsTo(
                DeleteSupplierTask.withSuppliers(idSupplier)
        );
    }
}
