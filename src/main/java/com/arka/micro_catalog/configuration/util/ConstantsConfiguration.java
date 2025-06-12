package com.arka.micro_catalog.configuration.util;

public class ConstantsConfiguration {
    private ConstantsConfiguration() {
        throw new IllegalStateException("Utility class");
    }

    public static final String VALIDATION_ERROR_CODE = "ERR_VALIDATION";
    public static final String INTERNAL_ERROR_PREFIX = "An unexpected error occurred: ";
    public static final String ERROR_PROCESSING_RESPONSE = "Error processing response";
    public static final String ERROR_WRITING_JSON = "Error writing error response as JSON";

    public static final String MIGRATE = "migrate";

    public static final String API_TITLE = "Micro Catalog API";
    public static final String API_DESCRIPTION = "API for catalog management";
    public static final String API_VERSION = "v1.0";
    public static final String API_GROUP = "catalog";
    public static final String API_PATHS = "/api/**";

    public static final String BEARER = "Bearer ";
    public static final int TOKEN_PREFIX_LENGTH = 7;
    public static final String ROLE_PREFIX = "ROLE_";
}
