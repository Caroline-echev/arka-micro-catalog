package com.arka.micro_catalog.domain.exception;

import com.arka.micro_catalog.domain.exception.error.CommonErrorCode;
import com.arka.micro_catalog.domain.exception.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@Builder
public class NotFoundException extends BusinessException {
    private static final ErrorCode DEFAULT_ERROR_CODE = CommonErrorCode.RESOURCE_NOT_FOUND;
    private static final String DEFAULT_CODE = DEFAULT_ERROR_CODE.getCode();
    private static final int STATUS_CODE = HttpStatus.NOT_FOUND.value();

    public NotFoundException(String message) {
        super(DEFAULT_ERROR_CODE, message);
    }

}