package com.testek.api.utilities;

public class Endpoints {
    // Basic url
    public static final String BASIC_URL = "http://tapi.testek.vn/api/v0/prod-man";

    // login url
    public static final String LOGIN = "/login-with-local";

    // category url
    public static final String CATEGORY_CREATE = "/category";
    public static final String CATEGORY_UPDATE = "/category/{id}";
    public static final String CATEGORY_DELETE = "/category/{categoryId}";
    public static final String CATEGORY_GET_BY_ID = "/category/{categoryId}/detail";
    public static final String CATEGORY_GET_ALL = "/category/search";

    // Supplier
    public static final String SUPPLER_CREATE = "/supplier";
    public static final String SUPPLER_UPDATE = "/supplier/{id}";
    public static final String SUPPLER_DELETE = "/supplier/{supplierId}";
    public static final String SUPPLER_GET_BY_ID = "/supplier/{supplierId}/detail";
    public static final String SUPPLER_GET_ALL = "/supplier/search";

    // Product
    public static final String PRODUCT_CREATE = "/product";
    public static final String PRODUCT_UPDATE = "/product/{productId}";
    public static final String PRODUCT_DELETE = "/product/{productId}";
    public static final String PRODUCT_GET_BY_ID = "/product/{productId}/detail";
    public static final String PRODUCT_GET_ALL = "/product/search";
}
