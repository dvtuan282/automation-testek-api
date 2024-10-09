package com.testek.api.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Data
public class CategoryModel {
    private String id;
    private String cateDesc;
    private String categoryName;
    private String status;

    public CategoryModel(String cateDesc, String categoryName, String status) {
        this.cateDesc = cateDesc;
        this.categoryName = categoryName;
        this.status = status;
    }

    public CategoryModel(String cateDesc, String categoryName) {
        this.cateDesc = cateDesc;
        this.categoryName = categoryName;
    }
}
