package com.arka.micro_catalog.adapters.driving.reactive.mapper;

import com.arka.micro_catalog.adapters.driving.reactive.dto.request.CategoryRequest;
import com.arka.micro_catalog.adapters.driving.reactive.dto.response.CategoryResponse;
import com.arka.micro_catalog.adapters.driving.reactive.dto.response.PaginationResponse;
import com.arka.micro_catalog.domain.model.CategoryModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
import org.mapstruct.Mapper;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;




@Mapper(componentModel = "spring")
public interface ICategoryDtoMapper {
    CategoryModel toModel(CategoryRequest request);

    CategoryResponse toResponse(CategoryModel model);

    default Mono<PaginationResponse<CategoryResponse>> toResponse(PaginationModel<CategoryModel> paginationModel) {
        PaginationResponse<CategoryResponse> response = new PaginationResponse<>();

        List<CategoryResponse> mappedItems = paginationModel.getItems()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        response.setItems(mappedItems);
        response.setTotalElements(paginationModel.getTotalElements());
        response.setCurrentPage(paginationModel.getCurrentPage());
        response.setTotalPages(paginationModel.getTotalPages());

        return Mono.just(response);
    }

}