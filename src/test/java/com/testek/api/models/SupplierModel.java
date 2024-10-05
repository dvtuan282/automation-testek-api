package com.testek.api.models;

public class SupplierModel {
    private String supAddress;
    private String supCity;
    private String supContactName;
    private String supCountry;
    private String supPhone;
    private String supPostalCode;
    private String supName;

    public SupplierModel() {
    }

    public SupplierModel(String supAddress, String supCity, String supContactName, String supCountry, String supPhone, String supPostalCode, String supName) {
        this.supAddress = supAddress;
        this.supCity = supCity;
        this.supContactName = supContactName;
        this.supCountry = supCountry;
        this.supPhone = supPhone;
        this.supPostalCode = supPostalCode;
        this.supName = supName;
    }

    public String getSupAddress() {
        return supAddress;
    }

    public void setSupAddress(String supAddress) {
        this.supAddress = supAddress;
    }

    public String getSupCity() {
        return supCity;
    }

    public void setSupCity(String supCity) {
        this.supCity = supCity;
    }

    public String getSupContactName() {
        return supContactName;
    }

    public void setSupContactName(String supContactName) {
        this.supContactName = supContactName;
    }

    public String getSupCountry() {
        return supCountry;
    }

    public void setSupCountry(String supCountry) {
        this.supCountry = supCountry;
    }

    public String getSupPhone() {
        return supPhone;
    }

    public void setSupPhone(String supPhone) {
        this.supPhone = supPhone;
    }

    public String getSupPostalCode() {
        return supPostalCode;
    }

    public void setSupPostalCode(String supPostalCode) {
        this.supPostalCode = supPostalCode;
    }

    public String getSupName() {
        return supName;
    }

    public void setSupName(String supName) {
        this.supName = supName;
    }
}
