package com.arka.micro_catalog.domain.enums;

import java.util.Arrays;

public enum ProductStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    OUT_OF_STOCK("OUT_OF_STOCK"),
    TO_BE_REPLENISHED("TO_BE_REPLENISHED");

    private final String value;

    ProductStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean isValid(String status) {
        return Arrays.stream(values())
                .anyMatch(s -> s.getValue().equalsIgnoreCase(status));
    }

    public static ProductStatus fromValue(String value) {
        return Arrays.stream(values())
                .filter(s -> s.getValue().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid product status: " + value));
    }
}
