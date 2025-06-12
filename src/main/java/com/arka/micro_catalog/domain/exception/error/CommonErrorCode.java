package com.arka.micro_catalog.domain.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommonErrorCode implements ErrorCode {
    INVALID_INPUT("ERR_INVALID_INPUT", "Invalid input data", HttpStatus.BAD_REQUEST.value(), ErrorCategory.VALIDATION),
    VALIDATION_ERROR("ERR_VALIDATION", "Validation error", HttpStatus.BAD_REQUEST.value(), ErrorCategory.VALIDATION),
    RESOURCE_NOT_FOUND("ERR_NOT_FOUND", "Resource not found", HttpStatus.NOT_FOUND.value(), ErrorCategory.RESOURCE),
    RESOURCE_ALREADY_EXISTS("ERR_DUPLICATE_RESOURCE", "Resource already exists", HttpStatus.CONFLICT.value(), ErrorCategory.RESOURCE),
    INTERNAL_ERROR("ERR_INTERNAL_SERVER", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCategory.SYSTEM);

    private final String code;
    private final String message;
    private final int statusCode;
    private final ErrorCategory category;
}