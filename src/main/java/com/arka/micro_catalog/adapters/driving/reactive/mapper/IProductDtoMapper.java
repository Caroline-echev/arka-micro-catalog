package com.arka.micro_catalog.adapters.driving.reactive.mapper;

import com.arka.micro_catalog.adapters.driving.reactive.dto.request.ProductRequest;
import com.arka.micro_catalog.domain.model.ProductModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IProductDtoMapper {
    ProductModel toModel(ProductRequest request);
}
