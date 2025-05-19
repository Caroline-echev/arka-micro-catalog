package com.arka.micro_catalog.adapters.driven.r2dbc.repository;

import com.arka.micro_catalog.adapters.driven.r2dbc.entity.CategoryEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ICategoryRepository extends ReactiveCrudRepository<CategoryEntity, Long> {
    Mono<CategoryEntity> findByName(String name);
}
