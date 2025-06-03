package com.arka.micro_catalog.adapters.driving.reactive.mapper;

import com.arka.micro_catalog.adapters.driving.reactive.dto.request.BrandRequest;
import com.arka.micro_catalog.adapters.driving.reactive.dto.response.BrandResponse;
import com.arka.micro_catalog.adapters.driving.reactive.dto.response.PaginationResponse;
import com.arka.micro_catalog.domain.model.BrandModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface IBrandDtoMapper {
    @Mapping(target = "id", ignore = true)
    BrandModel toModel(BrandRequest request);
    BrandResponse toResponse(BrandModel model);

    default Mono<PaginationResponse<BrandResponse>> toResponse(PaginationModel<BrandModel> paginationModel) {
        PaginationResponse<BrandResponse> response = new PaginationResponse<>();

        List<BrandResponse> mappedItems = paginationModel.getItems()
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
