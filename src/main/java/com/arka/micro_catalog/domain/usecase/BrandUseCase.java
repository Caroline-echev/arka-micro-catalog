package com.arka.micro_catalog.domain.usecase;


import com.arka.micro_catalog.domain.api.IBrandServicePort;
import com.arka.micro_catalog.domain.model.BrandModel;
import com.arka.micro_catalog.domain.model.CategoryModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
import com.arka.micro_catalog.domain.spi.IBrandPersistencePort;
import com.arka.micro_catalog.domain.util.validation.BrandValidator;
import com.arka.micro_catalog.domain.util.validation.CategoryValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BrandUseCase implements IBrandServicePort {

    private final IBrandPersistencePort brandPersistencePort;

    @Override
    public Mono<Void> createBrand(BrandModel brandModel) {
        return BrandValidator.validateBrandDoesNotExistByName(brandModel.getName(), brandPersistencePort)
                .then(brandPersistencePort.save(brandModel))
                .then();
    }

    @Override
    public Mono<PaginationModel<BrandModel>> getBrandsPaged(int page, int size, String sortDir, String search) {
        return brandPersistencePort.findAllPaged(page, size, sortDir, search);
    }

    @Override
    public Mono<BrandModel> getBrandById(Long id) {
        return BrandValidator.validateBrandExistsById(id, brandPersistencePort);
    }

    @Override
    public Mono<Void> updateBrand(Long id, BrandModel brandModel) {
        return BrandValidator.validateBrandExistsById(id, brandPersistencePort)
                .flatMap(existing -> BrandValidator
                        .validateBrandDoesNotExistByName(brandModel.getName(), brandPersistencePort)
                        .then(Mono.defer(() -> {
                            brandModel.setId(id);
                            return brandPersistencePort.save(brandModel);
                        }))
                )
                .then();
    }


}
