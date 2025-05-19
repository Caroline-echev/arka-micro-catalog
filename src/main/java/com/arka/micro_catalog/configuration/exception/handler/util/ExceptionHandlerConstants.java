package com.arka.micro_catalog.configuration.exception.handler.util;

public class ExceptionHandlerConstants {
    private ExceptionHandlerConstants() {
        throw new IllegalStateException("Utility class");
    }
    public static final String ERRORS = "errors";
    public static final String VALIDATION_ERROR_CODE = "ERR_";
    public static final String VALIDATION_ERROR_MESSAGE = "Errors in request body";

}
