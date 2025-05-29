package com.arka.micro_catalog.adapters.driving.reactive.controller;

import com.arka.micro_catalog.adapters.driving.reactive.dto.request.ProductRequest;
import com.arka.micro_catalog.adapters.driving.reactive.mapper.IProductDtoMapper;
import com.arka.micro_catalog.domain.api.IProductServicePort;
import com.arka.micro_catalog.domain.model.ProductModel;
import com.arka.micro_catalog.data.ProductData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private IProductServicePort productServicePort;

    @Mock
    private IProductDtoMapper productDtoMapper;

    @InjectMocks
    private ProductController productController;

    private ProductRequest request;
    private ProductModel model;

    @BeforeEach
    void setup() {
        request = ProductData.createProductRequest();
        model = ProductData.createProductModel();
    }

    @Test
    void createProduct_shouldReturnMonoVoid() {
        when(productDtoMapper.toModel(request)).thenReturn(model);
        when(productServicePort.createProduct(model, request.getBrandId(), request.getCategoryIds())).thenReturn(Mono.empty());

        Mono<Void> result = productController.createProduct(request);

        StepVerifier.create(result)
                .verifyComplete();

        verify(productServicePort).createProduct(model, request.getBrandId(), request.getCategoryIds());
    }
}
