package com.testek.api.models;

import java.util.UUID;

public class ProductModel {
    private UUID category;
    private UUID supplier;
    private String description;
    private String name;
    private Double price;
    private int quantity;
    private String unit;
    private String code;

    public ProductModel() {
    }

    public ProductModel(UUID category, UUID supplier, String description, String name, Double price, int quantity, String unit, String code) {
        this.category = category;
        this.supplier = supplier;
        this.description = description;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.unit = unit;
        this.code = code;
    }

    public UUID getCategory() {
        return category;
    }

    public void setCategory(UUID category) {
        this.category = category;
    }

    public UUID getSupplier() {
        return supplier;
    }

    public void setSupplier(UUID supplier) {
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
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
