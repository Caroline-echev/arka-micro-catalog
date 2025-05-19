package com.arka.micro_catalog.domain.api;

import com.arka.micro_catalog.domain.model.CategoryModel;
import reactor.core.publisher.Mono;

public interface ICategoryServicePort {
    Mono<Void> createCategory(CategoryModel categoryModel);
}
