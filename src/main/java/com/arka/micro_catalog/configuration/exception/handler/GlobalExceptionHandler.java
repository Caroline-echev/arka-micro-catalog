package com.arka.micro_catalog.configuration.exception.handler;

import com.arka.micro_catalog.domain.exception.BusinessException;
import com.arka.micro_catalog.domain.exception.error.CommonErrorCode;
import com.arka.micro_catalog.domain.exception.error.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.arka.micro_catalog.configuration.exception.handler.util.ExceptionHandlerConstants.VALIDATION_ERROR_CODE;
import static com.arka.micro_catalog.configuration.exception.handler.util.ExceptionHandlerConstants.*;
import static com.arka.micro_catalog.configuration.util.ConstantsConfiguration.*;


@Configuration
@Order(-2)
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("Handling exception: {}", ex.getClass().getName(), ex);

        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ErrorResponse errorResponse;

        if (ex instanceof BusinessException businessException) {
            exchange.getResponse().setStatusCode(HttpStatus.valueOf(businessException.getStatusCode()));
            errorResponse = new ErrorResponse(businessException.getCode(), businessException.getMessage());

        } else if (ex instanceof WebExchangeBindException bindException) {
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);

            Map<String, List<String>> fieldErrors = new HashMap<>();

            for (FieldError error : bindException.getBindingResult().getFieldErrors()) {
                String fieldName = error.getField();
                String errorMessage = error.getDefaultMessage();

                log.debug("Field error: {} -> {}", fieldName, errorMessage);

                fieldErrors.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(errorMessage);
            }

            errorResponse = ErrorResponse.builder()
                    .code(VALIDATION_ERROR_CODE)
                    .message(VALIDATION_ERROR_MESSAGE)
                    .details(Map.of(ERRORS, fieldErrors))
                    .build();

            log.debug("Complete validation exception: {}", bindException.getMessage());

        } else if (ex instanceof ResponseStatusException responseStatusException) {
            exchange.getResponse().setStatusCode(responseStatusException.getStatusCode());

            String code = VALIDATION_ERROR_CODE + responseStatusException.getStatusCode().value();
            String message = responseStatusException.getReason() != null ?
                    responseStatusException.getReason() : responseStatusException.getMessage();

            errorResponse = new ErrorResponse(code, message);

        } else {
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            ErrorCode errorCode = CommonErrorCode.INTERNAL_ERROR;
            errorResponse = new ErrorResponse(
                    errorCode.getCode(),
                    INTERNAL_ERROR_PREFIX + ex.getMessage()
            );
        }

        DataBuffer dataBuffer;
        try {
            log.debug("Error response: {}", objectMapper.writeValueAsString(errorResponse));
            dataBuffer = bufferFactory.wrap(objectMapper.writeValueAsBytes(errorResponse));
        } catch (JsonProcessingException e) {
            log.error(ERROR_WRITING_JSON, e);
            dataBuffer = bufferFactory.wrap(ERROR_PROCESSING_RESPONSE.getBytes());
        }

        return exchange.getResponse().writeWith(Mono.just(dataBuffer));
    }
}
