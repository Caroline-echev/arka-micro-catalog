package com.arka.micro_catalog.domain.usecase;


import com.arka.micro_catalog.domain.api.ICategoryServicePort;
import com.arka.micro_catalog.domain.exception.DuplicateResourceException;
import com.arka.micro_catalog.domain.model.CategoryModel;
import com.arka.micro_catalog.domain.spi.ICategoryPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class CategoryUseCase implements ICategoryServicePort {

    private final ICategoryPersistencePort categoryPersistencePort;


    @Override
    public Mono<Void> createCategory(CategoryModel categoryModel) {
        return categoryPersistencePort.findByName(categoryModel.getName())
                .flatMap(existingCategory -> Mono.error(new DuplicateResourceException("Category already exists")))
                .switchIfEmpty(categoryPersistencePort.save(categoryModel))
                .then();
    }

}
