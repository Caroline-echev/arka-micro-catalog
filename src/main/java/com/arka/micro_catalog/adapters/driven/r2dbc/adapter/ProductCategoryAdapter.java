package com.arka.micro_catalog.adapters.driven.r2dbc.adapter;

import com.arka.micro_catalog.adapters.driven.r2dbc.repository.IProductCategoryRepository;
import com.arka.micro_catalog.domain.spi.IProductCategoryPersistencePort;
import com.arka.micro_catalog.infrastructure.out.r2dbc.entity.ProductCategoryEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductCategoryAdapter implements IProductCategoryPersistencePort {

    private final IProductCategoryRepository productCategoryRepository;

    @Override
    public Mono<Void> saveProductCategories(Long productId, List<Long> categoryIds) {
        log.debug("Saving product-category relations for productId={} with categoryIds={}", productId, categoryIds);

        return Flux.fromIterable(categoryIds)
                .map(categoryId -> {
                    ProductCategoryEntity entity = ProductCategoryEntity.builder()
                            .productId(productId)
                            .categoryId(categoryId)
                            .build();
                    log.trace("Creating relation: {}", entity);
                    return entity;
                })
                .flatMap(productCategoryRepository::save)
                .doOnNext(saved -> log.trace("Saved relation: {}", saved))
                .then()
                .doOnSuccess(v -> log.debug("Finished saving all product-category relations for productId={}", productId));
    }

    @Override
    public Flux<Long> findCategoryIdsByProductId(Long productId) {
        log.debug("Fetching category IDs for productId={}", productId);

        return productCategoryRepository.findByProductId(productId)
                .doOnNext(entity -> log.trace("Fetched: {}", entity))
                .map(ProductCategoryEntity::getCategoryId);
    }

    @Override
    public Mono<Void> deleteByProductId(Long productId) {
        log.debug("Deleting all product-category relations for productId={}", productId);

        return productCategoryRepository.deleteByProductId(productId)
                .doOnSuccess(v -> log.debug("Deleted product-category relations for productId={}", productId));
    }
}
