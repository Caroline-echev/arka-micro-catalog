package com.arka.micro_catalog.adapters.driven.r2dbc.repository;



import com.arka.micro_catalog.infrastructure.out.r2dbc.entity.ProductCategoryEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface IProductCategoryRepository extends ReactiveCrudRepository<ProductCategoryEntity, Long> {
    Flux<ProductCategoryEntity> findByProductId(Long productId);
    Mono<Void> deleteByProductId(Long productId);
}