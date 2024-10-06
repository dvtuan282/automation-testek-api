package com.testek.api.features.supplierFeatures;

import com.testek.api.utilities.CategoryEndpoints;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SerenityJUnit5Extension.class)
public class GetSupplierTestCase {
    private static Actor actor;

    @BeforeAll
    static void setup() {
        actor = Actor.named("tuanTester").whoCan(CallAnApi.at(CategoryEndpoints.BASIC_URL));
    }

    @BeforeEach
    void LoginAndCreate() {

    }

}
