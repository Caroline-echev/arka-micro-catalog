package com.arka.micro_catalog.domain.exception.error;

public interface ErrorCode {
    String getCode();
    
    String getMessage();

    int getStatusCode();

    ErrorCategory getCategory();
}