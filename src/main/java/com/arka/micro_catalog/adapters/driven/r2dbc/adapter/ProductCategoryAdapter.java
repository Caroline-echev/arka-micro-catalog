package com.arka.micro_catalog.adapters.driven.r2dbc.adapter;

import com.arka.micro_catalog.adapters.driven.r2dbc.repository.IProductCategoryRepository;
import com.arka.micro_catalog.domain.spi.IProductCategoryPersistencePort;
import com.arka.micro_catalog.infrastructure.out.r2dbc.entity.ProductCategoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductCategoryAdapter implements IProductCategoryPersistencePort {

    private final IProductCategoryRepository productCategoryRepository;

    @Override
    public Mono<Void> saveProductCategories(Long productId, List<Long> categoryIds) {
        return Flux.fromIterable(categoryIds)
                .map(categoryId -> ProductCategoryEntity.builder()
                        .productId(productId)
                        .categoryId(categoryId)
                        .build())
                .flatMap(productCategoryRepository::save)
                .then();
    }

    @Override
    public Flux<Long> findCategoryIdsByProductId(Long productId) {
        return productCategoryRepository.findByProductId(productId)
                .map(ProductCategoryEntity::getCategoryId);
    }

    @Override
    public Mono<Void> deleteByProductId(Long productId) {
        return productCategoryRepository.deleteByProductId(productId);
    }
}