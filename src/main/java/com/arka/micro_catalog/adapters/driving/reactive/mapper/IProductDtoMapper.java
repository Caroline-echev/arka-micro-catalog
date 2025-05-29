package com.arka.micro_catalog.adapters.driving.reactive.mapper;

import com.arka.micro_catalog.adapters.driving.reactive.dto.request.ProductRequest;
import com.arka.micro_catalog.adapters.driving.reactive.dto.response.CategoryResponse;
import com.arka.micro_catalog.adapters.driving.reactive.dto.response.PaginationResponse;
import com.arka.micro_catalog.adapters.driving.reactive.dto.response.ProductResponse;
import com.arka.micro_catalog.domain.model.PaginationModel;
import com.arka.micro_catalog.domain.model.ProductModel;
import org.mapstruct.Mapper;
import com.arka.micro_catalog.domain.model.CategoryModel;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface IProductDtoMapper {
    ProductModel toModel(ProductRequest request);

    ProductResponse toResponse(ProductModel model);

    default Mono<PaginationResponse<ProductResponse>> toResponse(PaginationModel<ProductModel> paginationModel) {
        PaginationResponse<ProductResponse> response = new PaginationResponse<>();

        List<ProductResponse> mappedItems = paginationModel.getItems()
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
