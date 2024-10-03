package com.testek.api.models;

public class CategoryModel {
    private String cateDesc;
    private String categoryName;
    private String status;

    public CategoryModel() {
    }

    public CategoryModel(String cateDesc, String categoryName, String status) {
        this.cateDesc = cateDesc;
        this.categoryName = categoryName;
        this.status = status;
    }

    public String getCateDesc() {
        return cateDesc;
    }

    public void setCateDesc(String cateDesc) {
        this.cateDesc = cateDesc;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    } 
}
