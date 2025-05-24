package com.arka.micro_catalog.data;

import com.arka.micro_catalog.adapters.driven.r2dbc.entity.CategoryEntity;
import com.arka.micro_catalog.adapters.driving.reactive.dto.request.CategoryRequest;
import com.arka.micro_catalog.adapters.driving.reactive.dto.response.CategoryResponse;
import com.arka.micro_catalog.adapters.driving.reactive.dto.response.PaginationResponse;
import com.arka.micro_catalog.domain.model.CategoryModel;
import com.arka.micro_catalog.domain.model.PaginationModel;

import java.util.List;

public class CategoryData {

    public static CategoryModel createCategory() {
        return new CategoryModel(1L, "Electronics", "Electronics");
    }
    public static CategoryRequest createCategoryRequest() {
        return new CategoryRequest("Electronics", "Electronics");
    }

    public static CategoryEntity createCategoryEntity() {
        return new CategoryEntity(1L, "Electronics", "Electronics");
    }
    public static PaginationModel<CategoryModel> createPaginationModel() {
        return new PaginationModel<>(List.of(CategoryData.createCategory()), 1, 1, 1);
    }
    public static CategoryResponse createCategoryResponse() {
        return new CategoryResponse(1L, "Electronics", "Electronics");
    }
    public static PaginationResponse<CategoryResponse> createPaginationResponse() {
        return new PaginationResponse<>(List.of(CategoryData.createCategoryResponse()), 1, 1, 1);
    }
}
