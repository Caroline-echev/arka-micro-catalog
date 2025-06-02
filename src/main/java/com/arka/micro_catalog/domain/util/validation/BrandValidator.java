package com.arka.micro_catalog.domain.util.validation;

import com.arka.micro_catalog.domain.exception.DuplicateResourceException;
import com.arka.micro_catalog.domain.exception.NotFoundException;
import com.arka.micro_catalog.domain.model.BrandModel;
import com.arka.micro_catalog.domain.spi.IBrandPersistencePort;
import reactor.core.publisher.Mono;

import static com.arka.micro_catalog.domain.util.constants.BrandConstants.BRAND_ALREADY_EXISTS;
import static com.arka.micro_catalog.domain.util.constants.BrandConstants.BRAND_NOT_FOUND;


public class BrandValidator {


    public static Mono<Void> validateBrandDoesNotExistByName(String name, IBrandPersistencePort brandPersistencePort) {
        return brandPersistencePort.findByName(name)
                .flatMap(existing -> Mono.error(new DuplicateResourceException(BRAND_ALREADY_EXISTS)))
                .then();
    }

    public static Mono<BrandModel> validateBrandExistsById(Long brandId, IBrandPersistencePort brandPersistencePort) {
        return brandPersistencePort.findById(brandId)
                .switchIfEmpty(Mono.error(new NotFoundException(BRAND_NOT_FOUND)));
    }
}
