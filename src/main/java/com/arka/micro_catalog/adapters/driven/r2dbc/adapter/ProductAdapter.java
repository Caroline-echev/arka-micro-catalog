package com.arka.micro_catalog.adapters.driven.r2dbc.adapter;


import com.arka.micro_catalog.adapters.driven.r2dbc.mapper.IProductEntityMapper;
import com.arka.micro_catalog.adapters.driven.r2dbc.repository.IProductRepository;
import com.arka.micro_catalog.domain.model.BrandModel;
import com.arka.micro_catalog.domain.model.CategoryModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
import com.arka.micro_catalog.domain.model.ProductModel;
import com.arka.micro_catalog.domain.spi.IBrandPersistencePort;
import com.arka.micro_catalog.domain.spi.ICategoryPersistencePort;
import com.arka.micro_catalog.domain.spi.IProductCategoryPersistencePort;
import com.arka.micro_catalog.domain.spi.IProductPersistencePort;
import com.arka.micro_catalog.infrastructure.out.r2dbc.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.arka.micro_catalog.adapters.driven.r2dbc.util.ConstantsR2DBC.SORT_ASC;
import static com.arka.micro_catalog.adapters.driven.r2dbc.util.ConstantsR2DBC.SORT_DESC;

@Component
@RequiredArgsConstructor
public class ProductAdapter implements IProductPersistencePort {

    private final IProductRepository productRepository;
    private final IProductEntityMapper productEntityMapper;
    private final IBrandPersistencePort brandPersistencePort;
    private final ICategoryPersistencePort categoryPersistencePort;
    private final IProductCategoryPersistencePort productCategoryPersistencePort;

    @Override
    public Mono<ProductModel> save(ProductModel productModel) {
        ProductEntity productEntity = productEntityMapper.toEntity(productModel);

        return productRepository.save(productEntity)
                .map(savedEntity -> {
                    ProductModel savedModel = productEntityMapper.toModel(savedEntity);
                    savedModel.setBrand(productModel.getBrand());
                    savedModel.setCategories(productModel.getCategories());
                    return savedModel;
                });
    }

    @Override
    public Mono<ProductModel> findByName(String name) {
        return productRepository.findByName(name)
                .map(productEntityMapper::toModel);
    }

    @Override
    public Mono<ProductModel> findById(Long id) {
        return productRepository.findById(id)
                .map(productEntityMapper::toModel);
    }

    @Override
    public Mono<PaginationModel<ProductModel>> findAllPaged(int page, int size, String sortDir, String search) {
        long offset = (long) page * size;
        String normalizedSortDir = SORT_DESC.equalsIgnoreCase(sortDir) ? SORT_DESC : SORT_ASC;

        Flux<ProductEntity> entityFlux = productRepository.findAllPagedWithSearch(search, normalizedSortDir, size, offset);

        Flux<ProductModel> modelFlux = entityFlux.flatMap(entity -> {
            ProductModel model = productEntityMapper.toModel(entity);

            Mono<BrandModel> brandMono = brandPersistencePort.findById(entity.getBrandId()); // <-- cargar marca
            Flux<Long> categoryIdsFlux = productCategoryPersistencePort.findCategoryIdsByProductId(entity.getId()); // <-- cargar IDs

            Mono<List<CategoryModel>> categoriesMono = categoryIdsFlux
                    .flatMap(categoryPersistencePort::findById)
                    .collectList();

            return Mono.zip(Mono.just(model), brandMono, categoriesMono)
                    .map(tuple -> {
                        ProductModel enriched = tuple.getT1();
                        enriched.setBrand(tuple.getT2());
                        enriched.setCategories(tuple.getT3());
                        return enriched;
                    });
        });

        Mono<List<ProductModel>> itemsMono = modelFlux.collectList();
        Mono<Long> countMono = productRepository.countWithSearch(search);

        return Mono.zip(itemsMono, countMono)
                .map(tuple -> {
                    List<ProductModel> items = tuple.getT1();
                    long totalElements = tuple.getT2();
                    int totalPages = (int) Math.ceil((double) totalElements / size);

                    return PaginationModel.<ProductModel>builder()
                            .items(items)
                            .totalElements(totalElements)
                            .currentPage(page)
                            .totalPages(totalPages)
                            .build();
                });
    }


}
