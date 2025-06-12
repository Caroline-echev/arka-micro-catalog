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
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Flux<ProductModel> findAllPagedRaw(int page, int size, String sortDir, String search) {
        long offset = (long) page * size;
        String normalizedSortDir = SORT_DESC.equalsIgnoreCase(sortDir) ? SORT_DESC : SORT_ASC;

        return productRepository.findAllPagedWithSearch(search, normalizedSortDir, size, offset)
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
        return productRepository.countWithSearch(search);
    }


}
