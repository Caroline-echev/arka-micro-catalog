package com.arka.micro_catalog.adapters.driven.r2dbc.adapter;


import com.arka.micro_catalog.adapters.driven.r2dbc.mapper.IProductEntityMapper;
import com.arka.micro_catalog.adapters.driven.r2dbc.repository.IProductRepository;
import com.arka.micro_catalog.domain.model.ProductModel;
import com.arka.micro_catalog.domain.spi.IProductPersistencePort;
import com.arka.micro_catalog.infrastructure.out.r2dbc.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
@Component
@RequiredArgsConstructor
public class ProductAdapter implements IProductPersistencePort {

    private final IProductRepository productRepository;
    private final IProductEntityMapper productEntityMapper;


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
}