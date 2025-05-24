package com.arka.micro_catalog.adapters.driving.reactive.mapper;

import com.arka.micro_catalog.adapters.driving.reactive.dto.request.CategoryRequest;
import com.arka.micro_catalog.adapters.driving.reactive.dto.response.CategoryResponse;
import com.arka.micro_catalog.data.CategoryData;
import com.arka.micro_catalog.domain.model.CategoryModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

class ICategoryDtoMapperTest {

    private final ICategoryDtoMapper mapper = new ICategoryDtoMapper() {
        @Override
        public CategoryModel toModel(CategoryRequest request) {
            return null;
        }

        @Override
        public CategoryResponse toResponse(CategoryModel model) {
            return new CategoryResponse(model.getId(), model.getName(), model.getDescription());
        }
    };

    @Test
    void toResponse_shouldMapPaginationModelToPaginationResponse() {
        PaginationModel<CategoryModel> paginationModel = CategoryData.createPaginationModel();

        StepVerifier.create(mapper.toResponse(paginationModel))
                .expectNextMatches(response -> {
                    List<CategoryResponse> items = response.getItems();
                    return response.getTotalElements() == 1 &&
                            response.getCurrentPage() == 1 &&
                            response.getTotalPages() == 1 &&
                            items.size() == 1 &&
                            items.get(0).getName().equals("Electronics");
                })
                .verifyComplete();
    }
}
