
package com.arka.micro_catalog.domain.exception;

import com.arka.micro_catalog.domain.exception.error.CommonErrorCode;
import com.arka.micro_catalog.domain.exception.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpStatus;
@AllArgsConstructor
@Builder
public class BadRequestException extends BusinessException {
    private static final ErrorCode DEFAULT_ERROR_CODE = CommonErrorCode.INVALID_INPUT;
    private static final String DEFAULT_CODE = DEFAULT_ERROR_CODE.getCode();
    private static final int STATUS_CODE = HttpStatus.BAD_REQUEST.value();

    public BadRequestException(String message) {
        super(DEFAULT_ERROR_CODE, message);
    }


}