package com.arka.micro_catalog.adapters.driven.r2dbc.adapter;

import com.arka.micro_catalog.adapters.driven.r2dbc.mapper.IBrandEntityMapper;
import com.arka.micro_catalog.adapters.driven.r2dbc.repository.IBrandRepository;
import com.arka.micro_catalog.domain.model.BrandModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
import com.arka.micro_catalog.domain.spi.IBrandPersistencePort;
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
public class BrandAdapter implements IBrandPersistencePort {

    private final IBrandRepository brandRepository;
    private final IBrandEntityMapper brandEntityMapper;

    @Override
    public Mono<BrandModel> save(BrandModel user) {
        log.debug("Saving brand: {}", user);
        return brandRepository.save(brandEntityMapper.toEntity(user))
                .doOnNext(entity -> log.debug("Brand saved with ID: {}", entity.getId()))
                .map(brandEntityMapper::toModel);
    }

    @Override
    public Mono<BrandModel> findByName(String name) {
        log.debug("Finding brand by name: {}", name);
        return brandRepository.findByName(name)
                .doOnNext(entity -> log.debug("Brand found: {}", entity))
                .map(brandEntityMapper::toModel);
    }

    @Override
    public Mono<PaginationModel<BrandModel>> findAllPaged(int page, int size, String sortDir, String search) {
        long offset = (long) page * size;
        String normalizedSortDir = SORT_DESC.equalsIgnoreCase(sortDir) ? SORT_DESC : SORT_ASC;

        log.debug("Fetching paginated brands: page={}, size={}, sortDir={}, search='{}'", page, size, sortDir, search);

        Flux<BrandModel> itemsFlux = brandRepository
                .findAllPagedWithSearch(search, normalizedSortDir, size, offset)
                .map(brandEntityMapper::toModel);

        Mono<List<BrandModel>> itemsMono = itemsFlux
                .doOnNext(brand -> log.trace("Mapped brand: {}", brand))
                .collectList();

        Mono<Long> countMono = brandRepository.countWithSearch(search)
                .doOnNext(count -> log.debug("Total brands found for search='{}': {}", search, count));

        return Mono.zip(itemsMono, countMono)
                .map(tuple -> {
                    List<BrandModel> items = tuple.getT1();
                    long totalElements = tuple.getT2();
                    int totalPages = (int) Math.ceil((double) totalElements / size);

                    log.debug("Building PaginationModel: totalElements={}, totalPages={}", totalElements, totalPages);

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
        log.debug("Finding brand by ID: {}", id);
        return brandRepository.findById(id)
                .doOnNext(entity -> log.debug("Brand found with ID {}: {}", id, entity))
                .map(brandEntityMapper::toModel);
    }
}
