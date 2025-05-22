package com.arka.micro_catalog.domain.usecase;


import com.arka.micro_catalog.domain.api.ICategoryServicePort;
import com.arka.micro_catalog.domain.exception.DuplicateResourceException;
import com.arka.micro_catalog.domain.model.CategoryModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
import com.arka.micro_catalog.domain.spi.ICategoryPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.arka.micro_catalog.domain.util.constants.CategoryConstants.CATEGORY_ALREADY_EXISTS;
import static com.arka.micro_catalog.domain.util.constants.CategoryConstants.CATEGORY_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class CategoryUseCase implements ICategoryServicePort {

    private final ICategoryPersistencePort categoryPersistencePort;


    @Override
    public Mono<Void> createCategory(CategoryModel categoryModel) {
        return categoryPersistencePort.findByName(categoryModel.getName())
                .flatMap(existingCategory -> Mono.error(new DuplicateResourceException(CATEGORY_ALREADY_EXISTS)))
                .switchIfEmpty(categoryPersistencePort.save(categoryModel))
                .then();
    }
    @Override
    public Mono<PaginationModel<CategoryModel>> getCategoriesPaged(int page, int size, String sortDir, String search) {
        return categoryPersistencePort.findAllPaged(page, size, sortDir, search);
    }

    @Override
    public Mono<CategoryModel> getCategoryById(Long id) {
        return categoryPersistencePort.findById(id)
                .switchIfEmpty(Mono.error(new DuplicateResourceException(CATEGORY_NOT_FOUND)));
    }
}
