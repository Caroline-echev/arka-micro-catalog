package com.arka.micro_catalog.domain.exception;

import com.arka.micro_catalog.domain.exception.error.CommonErrorCode;
import com.arka.micro_catalog.domain.exception.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@Builder
public class DuplicateResourceException extends BusinessException {
    private static final ErrorCode DEFAULT_ERROR_CODE = CommonErrorCode.RESOURCE_ALREADY_EXISTS;
    private static final String DEFAULT_CODE = DEFAULT_ERROR_CODE.getCode();
    private static final int STATUS_CODE = HttpStatus.CONFLICT.value();

    public DuplicateResourceException(String message) {
        super(DEFAULT_ERROR_CODE, message);
    }

}