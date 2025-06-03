package com.arka.micro_catalog.adapters.driving.reactive.mapper;

import com.arka.micro_catalog.adapters.driving.reactive.dto.request.BrandRequest;
import com.arka.micro_catalog.adapters.driving.reactive.dto.response.BrandResponse;
import com.arka.micro_catalog.data.BrandData;
import com.arka.micro_catalog.domain.model.BrandModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

class IBrandDtoMapperTest {
    private final IBrandDtoMapper mapper = new IBrandDtoMapper() {
        @Override
        public BrandModel toModel(BrandRequest request) {
            return null;
        }

        @Override
        public BrandResponse toResponse(BrandModel model) {
            return new BrandResponse(model.getId(), model.getName(), model.getDescription());
        }
    };

    @Test
    void toResponse_shouldMapPaginationModelToPaginationResponse() {
        PaginationModel<BrandModel> paginationModel = BrandData.createPaginationModel();

        StepVerifier.create(mapper.toResponse(paginationModel))
                .expectNextMatches(response -> {
                    List<BrandResponse> items = response.getItems();
                    return response.getTotalElements() == 1 &&
                            response.getCurrentPage() == 1 &&
                            response.getTotalPages() == 1 &&
                            items.size() == 1 &&
                            items.get(0).getName().equals("Brand Test") &&
                            items.get(0).getDescription().equals("Description for Brand Test");
                })
                .verifyComplete();
    }

}
