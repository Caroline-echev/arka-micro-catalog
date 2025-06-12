package com.arka.micro_catalog.domain.exception;

import com.arka.micro_catalog.domain.exception.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessException extends RuntimeException {
    private ErrorCode errorCode;
    private String code;
    private String message;
    private int statusCode;


    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.code = errorCode.getCode();
        this.message = message;
        this.statusCode = errorCode.getStatusCode();
    }

}