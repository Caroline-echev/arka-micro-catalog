package com.arka.micro_catalog.adapters.driven.r2dbc.adapter;

import com.arka.micro_catalog.adapters.driven.r2dbc.mapper.ICategoryEntityMapper;
import com.arka.micro_catalog.adapters.driven.r2dbc.repository.ICategoryRepository;
import com.arka.micro_catalog.domain.model.CategoryModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
import com.arka.micro_catalog.domain.spi.ICategoryPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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

    @Override
    public Mono<PaginationModel<CategoryModel>> findAllPaged(int page, int size, String sortDir, String search) {
        long offset = (long) page * size;
        String normalizedSortDir = "desc".equalsIgnoreCase(sortDir) ? "desc" : "asc";

        Flux<CategoryModel> itemsFlux = categoryRepository
                .findAllPagedWithSearch(search, normalizedSortDir, size, offset)
                .map(categoryEntityMapper::toModel);

        Mono<List<CategoryModel>> itemsMono = itemsFlux.collectList();
        Mono<Long> countMono = categoryRepository.countWithSearch(search);

        return Mono.zip(itemsMono, countMono)
                .map(tuple -> {
                    List<CategoryModel> items = tuple.getT1();
                    long totalElements = tuple.getT2();
                    int totalPages = (int) Math.ceil((double) totalElements / size);

                    return PaginationModel.<CategoryModel>builder()
                            .items(items)
                            .totalElements(totalElements)
                            .currentPage(page)
                            .totalPages(totalPages)
                            .build();
                });
    }

}