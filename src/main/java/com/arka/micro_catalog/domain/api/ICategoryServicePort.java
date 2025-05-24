package com.arka.micro_catalog.domain.api;

import com.arka.micro_catalog.domain.model.CategoryModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
import reactor.core.publisher.Mono;

public interface ICategoryServicePort {
    Mono<Void> createCategory(CategoryModel categoryModel);
    Mono<PaginationModel<CategoryModel>> getCategoriesPaged(int page, int size, String sortDir, String search);
    Mono<CategoryModel> getCategoryById(Long id);
    Mono<Void> updateCategory(Long id, CategoryModel categoryModel);
}
