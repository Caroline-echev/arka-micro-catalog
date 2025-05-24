package com.arka.micro_catalog.adapters.driven.r2dbc.adapter;

import com.arka.micro_catalog.adapters.driven.r2dbc.mapper.IBrandEntityMapper;
import com.arka.micro_catalog.adapters.driven.r2dbc.mapper.ICategoryEntityMapper;
import com.arka.micro_catalog.adapters.driven.r2dbc.repository.IBrandRepository;
import com.arka.micro_catalog.adapters.driven.r2dbc.repository.ICategoryRepository;
import com.arka.micro_catalog.domain.model.BrandModel;
import com.arka.micro_catalog.domain.model.CategoryModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
import com.arka.micro_catalog.domain.spi.IBrandPersistencePort;
import com.arka.micro_catalog.domain.spi.ICategoryPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.arka.micro_catalog.adapters.driven.r2dbc.util.ConstantsR2DBC.SORT_ASC;
import static com.arka.micro_catalog.adapters.driven.r2dbc.util.ConstantsR2DBC.SORT_DESC;

@Component
@RequiredArgsConstructor
public class BrandAdapter implements IBrandPersistencePort {

    private final IBrandRepository brandRepository;
    private final IBrandEntityMapper brandEntityMapper;

    @Override
    public Mono<BrandModel> save(BrandModel user) {
        return brandRepository.save(brandEntityMapper.toEntity(user))
                .map(brandEntityMapper::toModel);
    }

    @Override
    public Mono<BrandModel> findByName(String name) {
        return brandRepository.findByName(name)
                .map(brandEntityMapper::toModel);

    }

    @Override
    public Mono<PaginationModel<BrandModel>> findAllPaged(int page, int size, String sortDir, String search) {
        long offset = (long) page * size;
        String normalizedSortDir = SORT_DESC.equalsIgnoreCase(sortDir) ? SORT_DESC : SORT_ASC;

        Flux<BrandModel> itemsFlux = brandRepository
                .findAllPagedWithSearch(search, normalizedSortDir, size, offset)
                .map(brandEntityMapper::toModel);

        Mono<List<BrandModel>> itemsMono = itemsFlux.collectList();
        Mono<Long> countMono = brandRepository.countWithSearch(search);

        return Mono.zip(itemsMono, countMono)
                .map(tuple -> {
                    List<BrandModel> items = tuple.getT1();
                    long totalElements = tuple.getT2();
                    int totalPages = (int) Math.ceil((double) totalElements / size);

                    return PaginationModel.<BrandModel>builder()
                            .items(items)
                            .totalElements(totalElements)
                            .currentPage(page)
                            .totalPages(totalPages)
                            .build();
                });
    }

    @Override
    public Mono<BrandModel> findById(Long id) {
        return brandRepository.findById(id)
                .map(brandEntityMapper::toModel);
    }
}