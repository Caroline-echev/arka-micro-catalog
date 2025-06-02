package com.arka.micro_catalog.domain.spi;

import com.arka.micro_catalog.domain.model.PaginationModel;
import com.arka.micro_catalog.domain.model.ProductModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IProductPersistencePort {
    Mono<ProductModel> save(ProductModel user);
    Mono<ProductModel> findByName(String name);

    Mono<ProductModel> findById(Long id);


    Flux<ProductModel> findAllPagedRaw(int page, int size, String sortDir, String search);
    Mono<Long> countWithSearch(String search);
}
