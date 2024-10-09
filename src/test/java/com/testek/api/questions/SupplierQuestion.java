package com.testek.api.questions;

import com.testek.api.models.SupplierModel;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class SupplierQuestion implements Question<String> {
    private final SupplierModel supplierRequest;

    public SupplierQuestion(SupplierModel supplierRequest) {
        this.supplierRequest = supplierRequest;
    }

    @Override
    public String answeredBy(Actor actor) {
        SupplierModel supplierResBody = SerenityRest.lastResponse().jsonPath().getObject("data", SupplierModel.class);
        SupplierModel supplierRes = new SupplierModel(
                supplierResBody.getSupAddress(),
                supplierResBody.getSupCity(),
                supplierResBody.getSupContactName(),
                supplierResBody.getSupCountry(),
                supplierResBody.getSupPhone(),
                supplierResBody.getSupPostalCode(),
                supplierResBody.getSupName()
        );
        System.out.println("supplierRequest: " + supplierRequest);
        System.out.println("supplierResponse: " + supplierRes);
        if (supplierRes.equals(supplierRequest)) {
            return "supplierRes match supplierReq";
        }
        return "supplierRes not match supplierReq";
    }

    public static SupplierQuestion responseSupp(SupplierModel supplierRequest) {
        return new SupplierQuestion(supplierRequest);
    }
}
