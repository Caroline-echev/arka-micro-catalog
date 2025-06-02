package com.arka.micro_catalog.domain.util.validation;

import com.arka.micro_catalog.domain.exception.BadRequestException;
import com.arka.micro_catalog.domain.exception.DuplicateResourceException;
import com.arka.micro_catalog.domain.exception.NotFoundException;
import com.arka.micro_catalog.domain.model.BrandModel;
import com.arka.micro_catalog.domain.model.CategoryModel;
import com.arka.micro_catalog.domain.model.ProductModel;
import com.arka.micro_catalog.domain.spi.IBrandPersistencePort;
import com.arka.micro_catalog.domain.spi.ICategoryPersistencePort;
import com.arka.micro_catalog.domain.spi.IProductPersistencePort;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.arka.micro_catalog.domain.util.constants.BrandConstants.BRAND_ALREADY_EXISTS;
import static com.arka.micro_catalog.domain.util.constants.BrandConstants.BRAND_NOT_FOUND;
import static com.arka.micro_catalog.domain.util.constants.ProductConstants.*;

public class ProductValidator {


    public static Mono<Void> validateProductDoesNotExistByName(String name, IProductPersistencePort productPersistencePort) {
        return productPersistencePort.findByName(name)
                .flatMap(existing -> Mono.error(new DuplicateResourceException(ERROR_PRODUCT_ALREADY_EXISTS)))
                .then();
    }

    public static Mono<ProductModel> validateProductExistsById(Long productId, IProductPersistencePort productPersistencePort) {
        return productPersistencePort.findById(productId)
                    .switchIfEmpty(Mono.error(new NotFoundException("PRODUCT_NOT_FOUND")));
    }



}
