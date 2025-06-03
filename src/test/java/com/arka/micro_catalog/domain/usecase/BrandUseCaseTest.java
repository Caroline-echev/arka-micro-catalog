package com.arka.micro_catalog.domain.usecase;

import com.arka.micro_catalog.data.BrandData;
import com.arka.micro_catalog.domain.model.BrandModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
import com.arka.micro_catalog.domain.spi.IBrandPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrandUseCaseTest {

    @Mock
    private IBrandPersistencePort brandPersistencePort;

    @InjectMocks
    private BrandUseCase brandUseCase;

    private BrandModel brandModel;

    @BeforeEach
    void setUp() {
        brandModel = BrandData.createBrandModel();
    }

    @Test
    void testCreateBrandSuccess() {
        when(brandPersistencePort.findByName(anyString())).thenReturn(Mono.empty());
        when(brandPersistencePort.save(any(BrandModel.class))).thenReturn(Mono.just(brandModel));

        StepVerifier.create(brandUseCase.createBrand(brandModel))
                .verifyComplete();

        verify(brandPersistencePort).save(brandModel);
    }

    @Test
    void testGetBrandsPaged() {
        PaginationModel<BrandModel> pagination = BrandData.createPaginationModel();
        when(brandPersistencePort.findAllPaged(0, 1, "asc", "Logi")).thenReturn(Mono.just(pagination));

        StepVerifier.create(brandUseCase.getBrandsPaged(0, 1, "asc", "Logi"))
                .expectNext(pagination)
                .verifyComplete();
    }

    @Test
    void testGetBrandByIdExists() {
        when(brandPersistencePort.findById(1L)).thenReturn(Mono.just(brandModel));

        StepVerifier.create(brandUseCase.getBrandById(1L))
                .expectNext(brandModel)
                .verifyComplete();
    }

    @Test
    void testUpdateBrandSuccess() {
        BrandModel updatedModel = new BrandModel();
        updatedModel.setName("HP");

        when(brandPersistencePort.findById(1L)).thenReturn(Mono.just(brandModel));
        when(brandPersistencePort.findByName("HP")).thenReturn(Mono.empty());
        when(brandPersistencePort.save(any(BrandModel.class))).thenReturn(Mono.just(updatedModel));

        StepVerifier.create(brandUseCase.updateBrand(1L, updatedModel))
                .verifyComplete();

        verify(brandPersistencePort).save(updatedModel);
    }
}
