package com.testek.api.models;

import lombok.*;

@EqualsAndHashCode
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierModel {
    private String id;
    private String supAddress;
    private String supCity;
    private String supContactName;
    private String supCountry;
    private String supPhone;
    private String supPostalCode;
    private String supName;

    public SupplierModel(String supAddress, String supCity, String supContactName, String supCountry, String supPhone, String supPostalCode, String supName) {
        this.supAddress = supAddress;
        this.supCity = supCity;
        this.supContactName = supContactName;
        this.supCountry = supCountry;
        this.supPhone = supPhone;
        this.supPostalCode = supPostalCode;
        this.supName = supName;
    }
}
