package com.arka.micro_catalog.domain.spi;

import com.arka.micro_catalog.domain.model.ProductModel;
import reactor.core.publisher.Mono;

public interface IProductPersistencePort {
    Mono<ProductModel> save(ProductModel user);
    Mono<ProductModel> findByName(String name);

    Mono<ProductModel> findById(Long id);

}
