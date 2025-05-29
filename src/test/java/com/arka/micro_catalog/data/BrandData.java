package com.arka.micro_catalog.data;

import com.arka.micro_catalog.adapters.driving.reactive.dto.request.BrandRequest;
import com.arka.micro_catalog.adapters.driving.reactive.dto.response.BrandResponse;
import com.arka.micro_catalog.domain.model.BrandModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
import com.arka.micro_catalog.adapters.driving.reactive.dto.response.PaginationResponse;

import java.util.List;

public class BrandData {

    public static BrandRequest createBrandRequest() {
        return new BrandRequest("Brand Test", "Description for Brand Test");
    }

    public static BrandModel createBrandModel() {
        return BrandModel.builder()
                .id(1L)
                .name("Brand Test")
                .description("Description for Brand Test")
                .build();
    }

    public static BrandResponse createBrandResponse() {
        return new BrandResponse(1L, "Brand Test", "Description for Brand Test");
    }

    public static PaginationModel<BrandModel> createPaginationModel() {
        return new PaginationModel<>(List.of(BrandData.createBrandModel()), 1, 1, 1);
    }

    public static PaginationResponse<BrandResponse> createPaginationResponse() {
        return new PaginationResponse<>(List.of(BrandData.createBrandResponse()), 1, 1, 1);
    }
}
