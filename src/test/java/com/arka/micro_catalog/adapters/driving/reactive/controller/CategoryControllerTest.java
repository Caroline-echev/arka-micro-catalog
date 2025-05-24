package com.arka.micro_catalog.adapters.driving.reactive.controller;

import com.arka.micro_catalog.adapters.driving.reactive.controller.CategoryController;
import com.arka.micro_catalog.adapters.driving.reactive.dto.request.CategoryRequest;
import com.arka.micro_catalog.adapters.driving.reactive.dto.response.CategoryResponse;
import com.arka.micro_catalog.adapters.driving.reactive.dto.response.PaginationResponse;
import com.arka.micro_catalog.adapters.driving.reactive.mapper.ICategoryDtoMapper;
import com.arka.micro_catalog.data.CategoryData;
import com.arka.micro_catalog.domain.api.ICategoryServicePort;
import com.arka.micro_catalog.domain.model.CategoryModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
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
class CategoryControllerTest {

    @Mock
    private ICategoryServicePort categoryServicePort;

    @Mock
    private ICategoryDtoMapper categoryDtoMapper;

    @InjectMocks
    private CategoryController categoryController;

    private CategoryRequest request;
    private CategoryModel model;
    private CategoryResponse response;

    @BeforeEach
    void setup() {
        request = CategoryData.createCategoryRequest();
        model =CategoryData.createCategory();
        response = CategoryData.createCategoryResponse();
    }

    @Test
    void createCategory_shouldReturnMonoVoid() {
        when(categoryDtoMapper.toModel(request)).thenReturn(model);
        when(categoryServicePort.createCategory(model)).thenReturn(Mono.empty());

        Mono<Void> result = categoryController.createCategory(request);

        StepVerifier.create(result)
                .verifyComplete();

        verify(categoryServicePort).createCategory(model);
    }

    @Test
    void getCategories_shouldReturnPaginationResponse() {
        PaginationModel<CategoryModel> paginationModel = CategoryData.createPaginationModel();
        PaginationResponse<CategoryResponse> paginationResponse = CategoryData.createPaginationResponse();

        when(categoryServicePort.getCategoriesPaged(0, 10, "asc", null)).thenReturn(Mono.just(paginationModel));
        when(categoryDtoMapper.toResponse(paginationModel)).thenReturn(Mono.just(paginationResponse));

        Mono<PaginationResponse<CategoryResponse>> result = categoryController.getCategories(0, 10, "asc", null);

        StepVerifier.create(result)
                .expectNext(paginationResponse)
                .verifyComplete();
    }

    @Test
    void getCategoryById_shouldReturnCategoryResponse() {
        when(categoryServicePort.getCategoryById(1L)).thenReturn(Mono.just(model));
        when(categoryDtoMapper.toResponse(model)).thenReturn(response);

        Mono<CategoryResponse> result = categoryController.getCategoryById(1L);

        StepVerifier.create(result)
                .expectNext(response)
                .verifyComplete();
    }

    @Test
    void updateCategory_shouldReturnMonoVoid() {
        when(categoryDtoMapper.toModel(request)).thenReturn(model);
        when(categoryServicePort.updateCategory(1L, model)).thenReturn(Mono.empty());

        Mono<Void> result = categoryController.updateCategory(1L, request);

        StepVerifier.create(result)
                .verifyComplete();

        verify(categoryServicePort).updateCategory(1L, model);
    }
}
