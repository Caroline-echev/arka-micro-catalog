package com.arka.micro_catalog.adapters.driving.reactive.controller;

import com.arka.micro_catalog.adapters.driving.reactive.dto.request.BrandRequest;
import com.arka.micro_catalog.adapters.driving.reactive.dto.response.BrandResponse;
import com.arka.micro_catalog.adapters.driving.reactive.dto.response.PaginationResponse;
import com.arka.micro_catalog.adapters.driving.reactive.mapper.IBrandDtoMapper;
import com.arka.micro_catalog.data.BrandData;
import com.arka.micro_catalog.domain.api.IBrandServicePort;
import com.arka.micro_catalog.domain.model.BrandModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrandControllerTest {

    @Mock
    private IBrandServicePort brandServicePort;

    @Mock
    private IBrandDtoMapper brandDtoMapper;

    @InjectMocks
    private BrandController brandController;

    private BrandRequest request;
    private BrandModel model;
    private BrandResponse response;

    @BeforeEach
    void setup() {
        request = BrandData.createBrandRequest();
        model = BrandData.createBrandModel();
        response = BrandData.createBrandResponse();
    }

    @Test
    void createBrand_shouldReturnMonoVoid() {
        when(brandDtoMapper.toModel(request)).thenReturn(model);
        when(brandServicePort.createBrand(model)).thenReturn(Mono.empty());

        Mono<Void> result = brandController.createBrand(request);

        StepVerifier.create(result)
                .verifyComplete();

        verify(brandServicePort).createBrand(model);
    }

    @Test
    void getBrands_shouldReturnPaginationResponse() {
        PaginationModel<BrandModel> paginationModel = BrandData.createPaginationModel();
        PaginationResponse<BrandResponse> paginationResponse = BrandData.createPaginationResponse();

        when(brandServicePort.getBrandsPaged(0, 2, "asc", null)).thenReturn(Mono.just(paginationModel));
        when(brandDtoMapper.toResponse(paginationModel)).thenReturn(Mono.just(paginationResponse));

        Mono<PaginationResponse<BrandResponse>> result = brandController.getBrands(0, 2, "asc", null);

        StepVerifier.create(result)
                .expectNext(paginationResponse)
                .verifyComplete();
    }

    @Test
    void getBrandById_shouldReturnBrandResponse() {
        when(brandServicePort.getBrandById(1L)).thenReturn(Mono.just(model));
        when(brandDtoMapper.toResponse(model)).thenReturn(response);

        Mono<BrandResponse> result = brandController.getBrandById(1L);

        StepVerifier.create(result)
                .expectNext(response)
                .verifyComplete();
    }

    @Test
    void updateBrand_shouldReturnMonoVoid() {
        when(brandDtoMapper.toModel(request)).thenReturn(model);
        when(brandServicePort.updateBrand(1L, model)).thenReturn(Mono.empty());

        Mono<Void> result = brandController.updateBrand(1L, request);

        StepVerifier.create(result)
                .verifyComplete();

        verify(brandServicePort).updateBrand(1L, model);
    }
}
