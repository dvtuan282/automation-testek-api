package com.testek.api.models;


public class ProductModel {
    private String category;
    private String supplier;
    private String description;
    private String name;
    private Double price;
    private Object quantity;
    private String unit;
    private String code;

    public ProductModel() {
    }

    public ProductModel(String category, String supplier, String description, String name, Double price, Object quantity, String unit, String code) {
        this.category = category;
        this.supplier = supplier;
        this.description = description;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.unit = unit;
        this.code = code;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Object getQuantity() {
        return quantity;
    }

    public void setQuantity(Object quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
