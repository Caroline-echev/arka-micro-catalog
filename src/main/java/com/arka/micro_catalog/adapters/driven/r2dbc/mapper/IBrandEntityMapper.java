package com.arka.micro_catalog.adapters.driven.r2dbc.mapper;


import com.arka.micro_catalog.adapters.driven.r2dbc.entity.BrandEntity;
import com.arka.micro_catalog.domain.model.BrandModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IBrandEntityMapper {

    BrandEntity toEntity(BrandModel categoryModel);

    BrandModel toModel(BrandEntity categoryEntity);
}
