package com.arka.micro_catalog.adapters.driving.reactive.mapper;

import com.arka.micro_catalog.adapters.driving.reactive.dto.response.ProductResponse;
import com.arka.micro_catalog.data.ProductData;
import com.arka.micro_catalog.domain.model.ProductModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

class IProductDtoMapperTest {

    private final IProductDtoMapper mapper = new IProductDtoMapper() {
        @Override
        public ProductModel toModel(com.arka.micro_catalog.adapters.driving.reactive.dto.request.ProductRequest request) {
            return null;
        }

        @Override
        public ProductResponse toResponse(ProductModel model) {
            return ProductResponse.builder()
                    .id(model.getId())
                    .name(model.getName())
                    .description(model.getDescription())
                    .price(model.getPrice())
                    .status(model.getStatus())
                    .brand(null)
                    .categories(null)
                    .build();
        }

    };

    @Test
    void toResponse_shouldMapPaginationModelToPaginationResponse() {
        PaginationModel<ProductModel> paginationModel = ProductData.createPaginationModel();

        StepVerifier.create(mapper.toResponse(paginationModel))
                .expectNextMatches(response -> {
                    List<ProductResponse> items = response.getItems();
                    return response.getTotalElements() == 1 &&
                            response.getCurrentPage() == 1 &&
                            response.getTotalPages() == 1 &&
                            items.size() == 1 &&
                            items.get(0).getName().equals("Name") &&
                            items.get(0).getDescription().equals("Product Test");
                })
                .verifyComplete();
    }

}
