package com.arka.micro_catalog.domain.spi;

import com.arka.micro_catalog.domain.model.BrandModel;
import com.arka.micro_catalog.domain.model.CategoryModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
import reactor.core.publisher.Mono;

public interface IBrandPersistencePort {
    Mono<BrandModel> save(BrandModel user);
    Mono<BrandModel> findByName(String name);
    Mono<PaginationModel<BrandModel>> findAllPaged(int page, int size, String sortDir, String search);

    Mono<BrandModel> findById(Long id);
}
