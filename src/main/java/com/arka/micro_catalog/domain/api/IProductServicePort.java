package com.arka.micro_catalog.domain.api;

import com.arka.micro_catalog.domain.model.PaginationModel;
import com.arka.micro_catalog.domain.model.ProductModel;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IProductServicePort {

    Mono<Void> createProduct(ProductModel productModel, Long brandId, List<Long> categoryIds);

    Mono<PaginationModel<ProductModel>> getProducts( int page,  int size,   String sortDir,    String searc);
    Mono<ProductModel> getProductById(Long id);
    Mono<ProductModel> updateProduct(Long productId, ProductModel productModel, Long brandId, List<Long> categoryIds);

}
