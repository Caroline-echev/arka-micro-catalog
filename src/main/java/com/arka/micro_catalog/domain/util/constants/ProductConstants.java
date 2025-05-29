package com.arka.micro_catalog.domain.util.constants;

public class ProductConstants {

    private ProductConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final int MAX_CATEGORY_COUNT = 3;
    public static final int MIN_CATEGORY_COUNT = 1;

    public static final String ERROR_BRAND_NOT_FOUND = "Brand not found";
    public static final String ERROR_CATEGORY_COUNT_INVALID = "Product must have 1 to 3 categories";
    public static final String ERROR_CATEGORY_DUPLICATE = "Product categories must not be repeated";
    public static final String ERROR_CATEGORIES_NOT_FOUND = "Some categories not found";

    public static final String ERROR_PRODUCT_ALREADY_EXISTS = "Product already exists";
}
