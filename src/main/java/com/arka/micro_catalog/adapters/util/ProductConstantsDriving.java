package com.arka.micro_catalog.adapters.util;

public class ProductConstantsDriving {
    private ProductConstantsDriving() {
        throw new IllegalStateException("Utility class");
    }

    public static final String NAME_REQUIRED = "Product name is required";
    public static final String NAME_MAX_MESSAGE = "Product name must not exceed 60 characters";
    public static final int NAME_MAX_LENGTH = 60;

    public static final String DESCRIPTION_REQUIRED = "Product description is required";
    public static final String DESCRIPTION_MAX_MESSAGE = "Product description must not exceed 200 characters";
    public static final int DESCRIPTION_MAX_LENGTH = 200;

    public static final String PRICE_REQUIRED = "Product price is required";
    public static final String PRICE_MIN_MESSAGE = "Product price must be greater than 0";
    public static final String PRICE_MIN_VALUE = "0.01";

    public static final String STATUS_REQUIRED = "Product status is required";

    public static final String PHOTO_REQUIRED = "Product photo is required";

    public static final String BRAND_ID_REQUIRED = "Brand ID is required";

    public static final String CATEGORY_IDS_REQUIRED = "At least one category is required";
    public static final String CATEGORY_IDS_SIZE_MESSAGE = "Product must have between 1 and 3 categories";
    public static final int CATEGORY_IDS_MIN = 1;
    public static final int CATEGORY_IDS_MAX = 3;
}
