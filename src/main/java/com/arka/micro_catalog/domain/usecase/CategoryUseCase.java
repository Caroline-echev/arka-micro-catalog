package com.arka.micro_catalog.domain.usecase;

import com.arka.micro_catalog.domain.api.ICategoryServicePort;
import com.arka.micro_catalog.domain.model.CategoryModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
import com.arka.micro_catalog.domain.spi.ICategoryPersistencePort;
import com.arka.micro_catalog.domain.util.validation.CategoryValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryUseCase implements ICategoryServicePort {

    private final ICategoryPersistencePort categoryPersistencePort;

    @Override
    public Mono<Void> createCategory(CategoryModel categoryModel) {
        log.debug("Creating category with name: {}", categoryModel.getName());
        return CategoryValidator.validateCategoryDoesNotExistByName(categoryModel.getName(), categoryPersistencePort)
                .doOnSuccess(v -> log.debug("Validation passed: category name '{}' is unique", categoryModel.getName()))
                .then(categoryPersistencePort.save(categoryModel))
                .doOnSuccess(saved -> log.info("Category '{}' saved successfully", categoryModel.getName()))
                .then();
    }

    @Override
    public Mono<PaginationModel<CategoryModel>> getCategoriesPaged(int page, int size, String sortDir, String search) {
        log.debug("Fetching categories - page: {}, size: {}, sortDir: {}, search: '{}'", page, size, sortDir, search);
        return categoryPersistencePort.findAllPaged(page, size, sortDir, search)
                .doOnSuccess(result -> log.debug("Found {} categories, total pages: {}",
                        result.getItems().size(), result.getTotalPages()));
    }

    @Override
    public Mono<CategoryModel> getCategoryById(Long id) {
        log.debug("Getting category by ID: {}", id);
        return CategoryValidator.validateCategoryExistsById(id, categoryPersistencePort)
                .doOnSuccess(category -> log.info("Category found: {}", category));
    }

    @Override
    public Mono<Void> updateCategory(Long id, CategoryModel categoryModel) {
        log.debug("Updating category ID: {} with new name: {}", id, categoryModel.getName());
        return CategoryValidator.validateCategoryExistsById(id, categoryPersistencePort)
                .doOnSuccess(existing -> log.debug("Category exists, proceeding with update."))
                .flatMap(existing -> CategoryValidator
                        .validateCategoryDoesNotExistByName(categoryModel.getName(), categoryPersistencePort)
                        .doOnSuccess(v -> log.debug("Category name '{}' is unique, proceeding to update", categoryModel.getName()))
                        .then(Mono.defer(() -> {
                            categoryModel.setId(id);
                            return categoryPersistencePort.save(categoryModel)
                                    .doOnSuccess(updated -> log.info("Category updated successfully: {}", updated));
                        }))
                )
                .then();
    }
}
