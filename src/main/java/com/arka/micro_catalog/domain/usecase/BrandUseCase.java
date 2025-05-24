package com.arka.micro_catalog.domain.usecase;


import com.arka.micro_catalog.domain.api.IBrandServicePort;
import com.arka.micro_catalog.domain.api.ICategoryServicePort;
import com.arka.micro_catalog.domain.exception.DuplicateResourceException;
import com.arka.micro_catalog.domain.exception.NotFoundException;
import com.arka.micro_catalog.domain.model.BrandModel;
import com.arka.micro_catalog.domain.model.CategoryModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
import com.arka.micro_catalog.domain.spi.IBrandPersistencePort;
import com.arka.micro_catalog.domain.spi.ICategoryPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.arka.micro_catalog.domain.util.constants.BrandConstants.BRAND_ALREADY_EXISTS;
import static com.arka.micro_catalog.domain.util.constants.BrandConstants.BRAND_NOT_FOUND;
import static com.arka.micro_catalog.domain.util.constants.CategoryConstants.CATEGORY_ALREADY_EXISTS;
import static com.arka.micro_catalog.domain.util.constants.CategoryConstants.CATEGORY_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class BrandUseCase implements IBrandServicePort {

    private final IBrandPersistencePort brandPersistencePort;



    @Override
    public Mono<Void> createBrand(BrandModel brandModel) {
        return brandPersistencePort.findByName(brandModel.getName())
                .flatMap(existingCategory -> Mono.error(new DuplicateResourceException(BRAND_ALREADY_EXISTS)))
                .switchIfEmpty(brandPersistencePort.save(brandModel))
                .then();
    }

    @Override
    public Mono<PaginationModel<BrandModel>> getBrandsPaged(int page, int size, String sortDir, String search) {
        return brandPersistencePort.findAllPaged(page, size, sortDir, search);
    }

    @Override
    public Mono<BrandModel> getBrandById(Long id) {
        return brandPersistencePort.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(BRAND_NOT_FOUND)));

    }

}
