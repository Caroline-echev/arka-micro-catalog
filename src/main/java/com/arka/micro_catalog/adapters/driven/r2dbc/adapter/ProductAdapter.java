package com.arka.micro_catalog.adapters.driven.r2dbc.adapter;

import com.arka.micro_catalog.adapters.driven.r2dbc.mapper.IProductEntityMapper;
import com.arka.micro_catalog.adapters.driven.r2dbc.repository.IProductRepository;
import com.arka.micro_catalog.domain.model.BrandModel;
import com.arka.micro_catalog.domain.model.ProductModel;
import com.arka.micro_catalog.domain.spi.IBrandPersistencePort;
import com.arka.micro_catalog.domain.spi.ICategoryPersistencePort;
import com.arka.micro_catalog.domain.spi.IProductCategoryPersistencePort;
import com.arka.micro_catalog.domain.spi.IProductPersistencePort;
import com.arka.micro_catalog.infrastructure.out.r2dbc.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.arka.micro_catalog.adapters.driven.r2dbc.util.ConstantsR2DBC.SORT_ASC;
import static com.arka.micro_catalog.adapters.driven.r2dbc.util.ConstantsR2DBC.SORT_DESC;

@Slf4j
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
        log.debug("Saving product: {}", productModel);

        ProductEntity productEntity = productEntityMapper.toEntity(productModel);

        return productRepository.save(productEntity)
                .doOnNext(saved -> log.debug("Product saved with ID: {}", saved.getId()))
                .map(savedEntity -> {
                    ProductModel savedModel = productEntityMapper.toModel(savedEntity);
                    savedModel.setBrand(productModel.getBrand());
                    savedModel.setCategories(productModel.getCategories());
                    return savedModel;
                });
    }

    @Override
    public Mono<ProductModel> findByName(String name) {
        log.debug("Finding product by name: {}", name);
        return productRepository.findByName(name)
                .doOnNext(entity -> log.debug("Product found: {}", entity))
                .map(productEntityMapper::toModel);
    }

    @Override
    public Mono<ProductModel> findById(Long id) {
        log.debug("Finding product by ID: {}", id);
        return productRepository.findById(id)
                .doOnNext(entity -> log.debug("Product found: {}", entity))
                .map(entity -> {
                    ProductModel model = productEntityMapper.toModel(entity);
                    if (entity.getBrandId() != null) {
                        log.debug("Setting brand ID {} to product model", entity.getBrandId());
                        BrandModel tempBrand = new BrandModel();
                        tempBrand.setId(entity.getBrandId());
                        model.setBrand(tempBrand);
                    }
                    return model;
                });
    }

    @Override
    public Flux<ProductModel> findAllPagedRaw(int page, int size, String sortDir, String search) {
        long offset = (long) page * size;
        String normalizedSortDir = SORT_DESC.equalsIgnoreCase(sortDir) ? SORT_DESC : SORT_ASC;

        log.debug("Fetching paged products: page={}, size={}, sortDir={}, search='{}'", page, size, normalizedSortDir, search);

        return productRepository.findAllPagedWithSearch(search, normalizedSortDir, size, offset)
                .doOnNext(entity -> log.trace("Product entity fetched: {}", entity))
                .map(entity -> {
                    ProductModel model = productEntityMapper.toModel(entity);
                    if (entity.getBrandId() != null) {
                        BrandModel tempBrand = new BrandModel();
                        tempBrand.setId(entity.getBrandId());
                        model.setBrand(tempBrand);
                    }
                    return model;
                });
    }

    @Override
    public Mono<Long> countWithSearch(String search) {
        log.debug("Counting products with search filter: '{}'", search);
        return productRepository.countWithSearch(search)
                .doOnNext(count -> log.debug("Total products matching '{}': {}", search, count));
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        log.debug("Checking if product exists with ID: {}", id);
        return productRepository.existsById(id)
                .doOnNext(exists -> log.debug("Product exists with ID {}: {}", id, exists));
    }
}
