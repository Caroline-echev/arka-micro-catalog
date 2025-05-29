package com.arka.micro_catalog.domain.util.validation;

import com.arka.micro_catalog.domain.exception.BadRequestException;
import com.arka.micro_catalog.domain.exception.NotFoundException;
import com.arka.micro_catalog.domain.model.BrandModel;
import com.arka.micro_catalog.domain.model.CategoryModel;
import com.arka.micro_catalog.domain.spi.IBrandPersistencePort;
import com.arka.micro_catalog.domain.spi.ICategoryPersistencePort;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.arka.micro_catalog.domain.util.constants.ProductConstants.*;

public class ProductValidationUtil {

    public static Mono<BrandModel> validateBrandExists(Long brandId, IBrandPersistencePort brandPersistencePort) {
        return brandPersistencePort.findById(brandId)
                .switchIfEmpty(Mono.error(new NotFoundException(ERROR_BRAND_NOT_FOUND)));
    }

    public static Mono<List<CategoryModel>> validateCategoriesExist(List<Long> categoryIds, ICategoryPersistencePort categoryPersistencePort) {
        return Mono.defer(() -> {
            if (categoryIds == null || categoryIds.isEmpty() || categoryIds.size() > MAX_CATEGORY_COUNT) {
                return Mono.error(new BadRequestException(ERROR_CATEGORY_COUNT_INVALID));
            }
            if (categoryIds.stream().distinct().count() != categoryIds.size()) {
                return Mono.error(new BadRequestException(ERROR_CATEGORY_DUPLICATE));
            }

            return categoryPersistencePort.findAllByIds(categoryIds)
                    .collectList()
                    .flatMap(categories -> {
                        if (categories.size() != categoryIds.size()) {
                            return Mono.error(new NotFoundException(ERROR_CATEGORIES_NOT_FOUND));
                        }
                        return Mono.just(categories);
                    });
        });
    }
}
