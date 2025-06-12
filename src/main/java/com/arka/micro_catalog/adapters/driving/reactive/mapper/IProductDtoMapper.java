
package com.arka.micro_catalog.adapters.driving.reactive.mapper;

import com.arka.micro_catalog.adapters.driving.reactive.dto.request.ProductRequest;
import com.arka.micro_catalog.adapters.driving.reactive.dto.response.PaginationResponse;
import com.arka.micro_catalog.adapters.driving.reactive.dto.response.ProductResponse;
import com.arka.micro_catalog.domain.model.PaginationModel;
import com.arka.micro_catalog.domain.model.ProductModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface IProductDtoMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "categories", ignore = true)
    ProductModel toModel(ProductRequest request);

    ProductResponse toResponse(ProductModel model);


    default Mono<PaginationResponse<ProductResponse>> toResponse(PaginationModel<ProductModel> paginationModel) {
        return Mono.fromCallable(() -> {
            PaginationResponse<ProductResponse> response = new PaginationResponse<>();

            List<ProductResponse> mappedItems = paginationModel.getItems()
                    .stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());

            response.setItems(mappedItems);
            response.setTotalElements(paginationModel.getTotalElements());
            response.setCurrentPage(paginationModel.getCurrentPage());
            response.setTotalPages(paginationModel.getTotalPages());

            return response;
        });
    }
}
