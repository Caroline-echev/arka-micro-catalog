package com.arka.micro_catalog.domain.usecase;

import com.arka.micro_catalog.domain.api.IBrandServicePort;
import com.arka.micro_catalog.domain.model.BrandModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
import com.arka.micro_catalog.domain.spi.IBrandPersistencePort;
import com.arka.micro_catalog.domain.util.validation.BrandValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandUseCase implements IBrandServicePort {

    private final IBrandPersistencePort brandPersistencePort;

    @Override
    public Mono<Void> createBrand(BrandModel brandModel) {
        log.debug("Creating brand: {}", brandModel.getName());
        return BrandValidator.validateBrandDoesNotExistByName(brandModel.getName(), brandPersistencePort)
                .doOnSuccess(v -> log.debug("Brand name validated, proceeding to save."))
                .then(brandPersistencePort.save(brandModel))
                .doOnSuccess(saved -> log.info("Brand saved successfully: {}", brandModel.getName()))
                .then();
    }

    @Override
    public Mono<PaginationModel<BrandModel>> getBrandsPaged(int page, int size, String sortDir, String search) {
        log.debug("Fetching brands with pagination - page: {}, size: {}, sortDir: {}, search: {}", page, size, sortDir, search);
        return brandPersistencePort.findAllPaged(page, size, sortDir, search)
                .doOnSuccess(result -> log.debug("Fetched {} brands, total pages: {}", result.getItems().size(), result.getTotalPages()));
    }

    @Override
    public Mono<BrandModel> getBrandById(Long id) {
        log.debug("Fetching brand by ID: {}", id);
        return BrandValidator.validateBrandExistsById(id, brandPersistencePort)
                .doOnSuccess(brand -> log.info("Brand found with ID: {}", id));
    }

    @Override
    public Mono<Void> updateBrand(Long id, BrandModel brandModel) {
        log.debug("Updating brand ID: {} with new name: {}", id, brandModel.getName());
        return BrandValidator.validateBrandExistsById(id, brandPersistencePort)
                .doOnSuccess(existing -> log.debug("Brand exists with ID: {}, proceeding with update validation.", id))
                .flatMap(existing -> BrandValidator
                        .validateBrandDoesNotExistByName(brandModel.getName(), brandPersistencePort)
                        .doOnSuccess(v -> log.debug("Brand name '{}' is unique, proceeding with update.", brandModel.getName()))
                        .then(Mono.defer(() -> {
                            brandModel.setId(id);
                            return brandPersistencePort.save(brandModel)
                                    .doOnSuccess(updated -> log.info("Brand updated successfully: {}", brandModel));
                        }))
                )
                .then();
    }
}
