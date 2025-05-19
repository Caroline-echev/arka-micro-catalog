package com.arka.micro_catalog.domain.spi;

import com.arka.micro_catalog.domain.model.CategoryModel;
import reactor.core.publisher.Mono;

public interface ICategoryPersistencePort {
    Mono<CategoryModel> save(CategoryModel user);
    Mono<CategoryModel> findByName(String name);

}
