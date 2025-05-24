package com.arka.micro_catalog.adapters.driven.r2dbc.repository;

import com.arka.micro_catalog.adapters.driven.r2dbc.entity.BrandEntity;
import com.arka.micro_catalog.adapters.driven.r2dbc.entity.CategoryEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface IBrandRepository extends ReactiveCrudRepository<BrandEntity, Long> {
    Mono<BrandEntity> findByName(String name);


}
