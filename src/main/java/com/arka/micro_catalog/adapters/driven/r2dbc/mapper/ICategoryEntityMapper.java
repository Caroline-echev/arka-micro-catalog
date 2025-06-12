package com.arka.micro_catalog.adapters.driven.r2dbc.mapper;


import com.arka.micro_catalog.adapters.driven.r2dbc.entity.CategoryEntity;
import com.arka.micro_catalog.domain.model.CategoryModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ICategoryEntityMapper {

    CategoryEntity toEntity(CategoryModel categoryModel);

    CategoryModel toModel(CategoryEntity categoryEntity);
}
