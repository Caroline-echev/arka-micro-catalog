package com.arka.micro_catalog.domain.spi;

import com.arka.micro_catalog.domain.model.BrandModel;
import com.arka.micro_catalog.domain.model.CategoryModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
import reactor.core.publisher.Mono;

public interface IBrandPersistencePort {
    Mono<BrandModel> save(BrandModel user);
    Mono<BrandModel> findByName(String name);


}
