package com.arka.micro_catalog.configuration.exception.handler;

import com.arka.micro_catalog.configuration.exception.handler.ErrorResponse;
import com.arka.micro_catalog.domain.exception.BusinessException;
import com.arka.micro_catalog.domain.exception.error.CommonErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    private MockServerWebExchange exchange;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/").build());

    }

    @Test
    void handle_shouldReturnBusinessExceptionResponse() throws Exception {

        BusinessException businessException = new BusinessException(
                CommonErrorCode.INTERNAL_ERROR,
                "Business error occurred"
        );

        ErrorResponse expectedResponse = new ErrorResponse(
                businessException.getCode(),
                businessException.getMessage()
        );

        when(objectMapper.writeValueAsBytes(any(ErrorResponse.class)))
                .thenReturn("{\"code\":\"500\",\"message\":\"Business error occurred\"}".getBytes());

        Mono<Void> result = globalExceptionHandler.handle(exchange, businessException);


        StepVerifier.create(result).verifyComplete();

        MockServerHttpResponse response = (MockServerHttpResponse) exchange.getResponse();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }



    @Test
    void handle_shouldReturnResponseStatusExceptionResponse() throws Exception {
        ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");

        when(objectMapper.writeValueAsBytes(any(ErrorResponse.class))).thenReturn("{\"code\":\"VALIDATION_ERROR404\",\"message\":\"Not Found\"}".getBytes());

        Mono<Void> result = globalExceptionHandler.handle(exchange, responseStatusException);

        StepVerifier.create(result)
                .verifyComplete();

        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exchange.getResponse().getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }

    @Test
    void handle_shouldReturnValidationErrorResponse() throws Exception {
        BindingResult bindingResult = mock(BindingResult.class);
        List<FieldError> fieldErrors = List.of(
                new FieldError("objectName", "field1", "must not be null"),
                new FieldError("objectName", "field2", "must be a valid email")
        );
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        // Mock del WebExchangeBindException con el bindingResult simulado
        WebExchangeBindException bindException = mock(WebExchangeBindException.class);
        when(bindException.getBindingResult()).thenReturn(bindingResult);
        when(bindException.getMessage()).thenReturn("Validation failed");

        // El objectMapper debe serializar el error esperado
        when(objectMapper.writeValueAsBytes(any(ErrorResponse.class)))
                .thenReturn("{\"code\":\"VALIDATION_ERROR\",\"message\":\"Validation failed\",\"details\":{\"errors\":{\"field1\":[\"must not be null\"],\"field2\":[\"must be a valid email\"]}}}".getBytes());

        Mono<Void> result = globalExceptionHandler.handle(exchange, bindException);

        StepVerifier.create(result)
                .verifyComplete();

        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exchange.getResponse().getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }
    @Test
    void handle_shouldReturnInternalServerErrorForUnknownException() throws Exception {
        Exception exception = new Exception("Unexpected error");

        when(objectMapper.writeValueAsBytes(any(ErrorResponse.class))).thenReturn("{\"code\":\"INTERNAL_ERROR\",\"message\":\"Internal Server Error: Unexpected error\"}".getBytes());

        Mono<Void> result = globalExceptionHandler.handle(exchange, exception);

        StepVerifier.create(result)
                .verifyComplete();

        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(exchange.getResponse().getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }


}
