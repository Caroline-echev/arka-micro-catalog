package com.arka.micro_catalog.domain.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCategory {
    VALIDATION("Validation errors"),
    RESOURCE("Resource-related errors"),
    SYSTEM("System errors");

    private final String description;

}