package com.arka.micro_catalog.adapters.driven.r2dbc.repository;

import com.arka.micro_catalog.infrastructure.out.r2dbc.entity.ProductEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface IProductRepository extends ReactiveCrudRepository<ProductEntity, Long> {
    Mono<ProductEntity> findByName(String name);
}
