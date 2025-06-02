package com.arka.micro_catalog.domain.util.validation;

import com.arka.micro_catalog.domain.exception.BadRequestException;
import com.arka.micro_catalog.domain.exception.DuplicateResourceException;
import com.arka.micro_catalog.domain.exception.NotFoundException;
import com.arka.micro_catalog.domain.model.CategoryModel;
import com.arka.micro_catalog.domain.spi.ICategoryPersistencePort;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.arka.micro_catalog.domain.util.constants.CategoryConstants.CATEGORY_ALREADY_EXISTS;
import static com.arka.micro_catalog.domain.util.constants.CategoryConstants.CATEGORY_NOT_FOUND;
import static com.arka.micro_catalog.domain.util.constants.ProductConstants.*;
import static com.arka.micro_catalog.domain.util.constants.ProductConstants.ERROR_CATEGORIES_NOT_FOUND;

public class CategoryValidator {
    public static Mono<Void> validateCategoryDoesNotExistByName(String name, ICategoryPersistencePort categoryPersistencePort) {
        return categoryPersistencePort.findByName(name)
                .flatMap(existing -> Mono.error(new DuplicateResourceException(CATEGORY_ALREADY_EXISTS)))
                .then();
    }

    public static Mono<CategoryModel> validateCategoryExistsById(Long categoryId, ICategoryPersistencePort categoryPersistencePort) {
        return categoryPersistencePort.findById(categoryId)
                .switchIfEmpty(Mono.error(new NotFoundException(CATEGORY_NOT_FOUND)));
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
