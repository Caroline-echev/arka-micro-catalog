package com.arka.micro_catalog.adapters.driven.r2dbc.mapper;

import com.arka.micro_catalog.domain.model.ProductModel;
import com.arka.micro_catalog.infrastructure.out.r2dbc.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IProductEntityMapper {

    @Mapping(target = "brandId", source = "brand.id")
    ProductEntity toEntity(ProductModel model);

    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "categories", ignore = true)
    ProductModel toModel(ProductEntity entity);
}