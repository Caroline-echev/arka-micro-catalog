package com.arka.micro_catalog.adapters.util;

public class CategoryConstantsDriving {
    private CategoryConstantsDriving() {
        throw new IllegalStateException("Utility class");
    }
    public static final String PAGE_DEFAULT = "0";
    public static final String SIZE_DEFAULT = "2";
    public static final String SORT_DEFAULT = "asc";
    public static final int MAX_PAGE_SIZE = 100;
    public static final String NAME_NOT_BLANK = "Name cannot be blank";
    public static final String NAME_MAX_SIZE =  "Name cannot be longer than 100 characters";
    public static final String DESCRIPTION_NOT_BLANK = "Description cannot be blank";
}
