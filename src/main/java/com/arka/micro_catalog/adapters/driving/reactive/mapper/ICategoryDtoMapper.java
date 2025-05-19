package com.arka.micro_catalog.adapters.driving.reactive.mapper;

import com.arka.micro_catalog.adapters.driving.reactive.dto.request.CategoryRequest;
import com.arka.micro_catalog.domain.model.CategoryModel;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ICategoryDtoMapper {
    CategoryModel toModel(CategoryRequest request);
}

