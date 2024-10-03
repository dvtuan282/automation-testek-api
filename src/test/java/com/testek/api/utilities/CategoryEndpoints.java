package com.testek.api.utilities;

public class CategoryEndpoints {
    // Basic url
    public static final String BASIC_URL = "http://tapi.testek.vn/api/v0/prod-man";

    // login url
    public static final String LOGIN = "/login-with-local";
    // category url
    public static final String CATEGORY_CREATE = "/category";
    public static final String CATEGORY_UPDATE = "/category/{id}";
    public static final String CATEGORY_DELETE = "/category/{categoryId}";
    public static final String CATEGORY_GET_BY_ID = "/category/{category}/detail";
    public static final String CATEGORY_GET_ALL = "/category/search";
}
