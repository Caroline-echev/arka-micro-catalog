package com.arka.micro_catalog.domain.usecase;


import com.arka.micro_catalog.domain.api.IProductServicePort;
import com.arka.micro_catalog.domain.exception.DuplicateResourceException;
import com.arka.micro_catalog.domain.model.ProductModel;
import com.arka.micro_catalog.domain.spi.IBrandPersistencePort;
import com.arka.micro_catalog.domain.spi.ICategoryPersistencePort;
import com.arka.micro_catalog.domain.spi.IProductCategoryPersistencePort;
import com.arka.micro_catalog.domain.spi.IProductPersistencePort;
import com.arka.micro_catalog.domain.util.validation.ProductValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.arka.micro_catalog.domain.util.constants.ProductConstants.ERROR_PRODUCT_ALREADY_EXISTS;


@Service
@RequiredArgsConstructor
public class ProductUseCase implements IProductServicePort {
    private final IProductPersistencePort productPersistencePort;
    private final ICategoryPersistencePort categoryPersistencePort;
    private final IBrandPersistencePort brandPersistencePort;
    private final IProductCategoryPersistencePort productCategoryPersistencePort;


    @Override
    public Mono<Void> createProduct(ProductModel productModel, Long brandId, List<Long> categoryIds) {
        return checkProductExists(productModel.getName())
                .then(ProductValidationUtil.validateBrandExists(brandId, brandPersistencePort)
                        .flatMap(brand -> ProductValidationUtil.validateCategoriesExist(categoryIds, categoryPersistencePort)
                                .flatMap(categories -> {
                                    productModel.setBrand(brand);
                                    productModel.setCategories(categories);

                                    return productPersistencePort.save(productModel)
                                            .flatMap(savedProduct ->
                                                    productCategoryPersistencePort.saveProductCategories(
                                                            savedProduct.getId(),
                                                            categoryIds
                                                    )
                                            );
                                })));
    }

    private Mono<Void> checkProductExists(String productName) {
        return productPersistencePort.findByName(productName)
                .flatMap(existing -> Mono.error(new DuplicateResourceException(ERROR_PRODUCT_ALREADY_EXISTS)))
                .then();
    }
}
