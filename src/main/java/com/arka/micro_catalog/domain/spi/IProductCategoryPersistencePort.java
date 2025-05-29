
package com.arka.micro_catalog.domain.spi;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IProductCategoryPersistencePort {
    Mono<Void> saveProductCategories(Long productId, List<Long> categoryIds);
    Flux<Long> findCategoryIdsByProductId(Long productId);
    Mono<Void> deleteByProductId(Long productId);
}
