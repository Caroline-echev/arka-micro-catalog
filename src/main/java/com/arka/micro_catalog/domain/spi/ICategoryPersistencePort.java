package com.arka.micro_catalog.domain.spi;

import com.arka.micro_catalog.domain.model.CategoryModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
import reactor.core.publisher.Mono;

public interface ICategoryPersistencePort {
    Mono<CategoryModel> save(CategoryModel user);
    Mono<CategoryModel> findByName(String name);
    Mono<PaginationModel<CategoryModel>> findAllPaged(int page, int size, String sortDir, String search);
    Mono<CategoryModel> findById(Long id);

}
