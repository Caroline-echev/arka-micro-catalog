package com.arka.micro_catalog.adapters.driven.r2dbc.adapter;

import com.arka.micro_catalog.adapters.driven.r2dbc.mapper.ICategoryEntityMapper;
import com.arka.micro_catalog.adapters.driven.r2dbc.repository.ICategoryRepository;
import com.arka.micro_catalog.domain.model.CategoryModel;
import com.arka.micro_catalog.domain.spi.ICategoryPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CategoryAdapter implements ICategoryPersistencePort {

    private final ICategoryRepository categoryRepository;
    private final ICategoryEntityMapper categoryEntityMapper;

    @Override
    public Mono<CategoryModel> save(CategoryModel user) {
        return categoryRepository.save(categoryEntityMapper.toEntity(user))
                .map(categoryEntityMapper::toModel);
    }

    @Override
    public Mono<CategoryModel> findByName(String name) {
        return categoryRepository.findByName(name)
                .map(categoryEntityMapper::toModel);

    }
}
