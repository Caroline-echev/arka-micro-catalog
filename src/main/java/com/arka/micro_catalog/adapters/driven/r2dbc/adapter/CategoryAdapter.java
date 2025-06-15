package com.arka.micro_catalog.adapters.driven.r2dbc.adapter;

import com.arka.micro_catalog.adapters.driven.r2dbc.mapper.ICategoryEntityMapper;
import com.arka.micro_catalog.adapters.driven.r2dbc.repository.ICategoryRepository;
import com.arka.micro_catalog.domain.model.CategoryModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
import com.arka.micro_catalog.domain.spi.ICategoryPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.arka.micro_catalog.adapters.driven.r2dbc.util.ConstantsR2DBC.SORT_ASC;
import static com.arka.micro_catalog.adapters.driven.r2dbc.util.ConstantsR2DBC.SORT_DESC;

@Slf4j
@Component
@RequiredArgsConstructor
public class CategoryAdapter implements ICategoryPersistencePort {

    private final ICategoryRepository categoryRepository;
    private final ICategoryEntityMapper categoryEntityMapper;

    @Override
    public Mono<CategoryModel> save(CategoryModel user) {
        log.debug("Saving category: {}", user);
        return categoryRepository.save(categoryEntityMapper.toEntity(user))
                .doOnNext(entity -> log.debug("Category saved with ID: {}", entity.getId()))
                .map(categoryEntityMapper::toModel);
    }

    @Override
    public Mono<CategoryModel> findByName(String name) {
        log.debug("Searching for category with name: {}", name);
        return categoryRepository.findByName(name)
                .doOnNext(entity -> log.debug("Category found: {}", entity))
                .map(categoryEntityMapper::toModel);
    }

    @Override
    public Mono<PaginationModel<CategoryModel>> findAllPaged(int page, int size, String sortDir, String search) {
        long offset = (long) page * size;
        String normalizedSortDir = SORT_DESC.equalsIgnoreCase(sortDir) ? SORT_DESC : SORT_ASC;

        log.debug("Fetching categories paginated: page={}, size={}, sortDir={}, search='{}'", page, size, normalizedSortDir, search);

        Flux<CategoryModel> itemsFlux = categoryRepository
                .findAllPagedWithSearch(search, normalizedSortDir, size, offset)
                .map(categoryEntityMapper::toModel)
                .doOnNext(cat -> log.trace("Mapped category: {}", cat));

        Mono<List<CategoryModel>> itemsMono = itemsFlux.collectList();

        Mono<Long> countMono = categoryRepository.countWithSearch(search)
                .doOnNext(count -> log.debug("Total categories found for search='{}': {}", search, count));

        return Mono.zip(itemsMono, countMono)
                .map(tuple -> {
                    List<CategoryModel> items = tuple.getT1();
                    long totalElements = tuple.getT2();
                    int totalPages = (int) Math.ceil((double) totalElements / size);

                    log.debug("Pagination result -> totalElements: {}, totalPages: {}", totalElements, totalPages);

                    return PaginationModel.<CategoryModel>builder()
                            .items(items)
                            .totalElements(totalElements)
                            .currentPage(page)
                            .totalPages(totalPages)
                            .build();
                });
    }

    @Override
    public Mono<CategoryModel> findById(Long id) {
        log.debug("Finding category by ID: {}", id);
        return categoryRepository.findById(id)
                .doOnNext(entity -> log.debug("Category found: {}", entity))
                .map(categoryEntityMapper::toModel);
    }

    @Override
    public Flux<CategoryModel> findAllByIds(List<Long> ids) {
        log.debug("Fetching categories by IDs: {}", ids);
        return categoryRepository.findAllByIdIn(ids)
                .doOnNext(entity -> log.trace("Fetched category: {}", entity))
                .map(categoryEntityMapper::toModel);
    }
}
