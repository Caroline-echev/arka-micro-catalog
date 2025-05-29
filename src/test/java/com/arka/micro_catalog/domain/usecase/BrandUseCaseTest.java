package com.arka.micro_catalog.domain.usecase;

import com.arka.micro_catalog.data.BrandData;
import com.arka.micro_catalog.domain.api.IBrandServicePort;
import com.arka.micro_catalog.domain.exception.DuplicateResourceException;
import com.arka.micro_catalog.domain.exception.NotFoundException;
import com.arka.micro_catalog.domain.model.BrandModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
import com.arka.micro_catalog.domain.spi.IBrandPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static com.arka.micro_catalog.domain.util.constants.BrandConstants.BRAND_ALREADY_EXISTS;
import static com.arka.micro_catalog.domain.util.constants.BrandConstants.BRAND_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void createBrand_WhenBrandDoesNotExist_ShouldSaveBrand() {
        when(brandPersistencePort.findByName(brandModel.getName())).thenReturn(Mono.empty());
        when(brandPersistencePort.save(any(BrandModel.class))).thenReturn(Mono.just(brandModel));

        StepVerifier.create(brandUseCase.createBrand(brandModel))
                .verifyComplete();

        verify(brandPersistencePort).findByName(brandModel.getName());
        verify(brandPersistencePort).save(brandModel);
    }
    @Test
    void getBrandsPaged_ShouldReturnPagedBrands() {
        int page = 0;
        int size = 2;
        String sortDir = "asc";
        String search = "test";

        PaginationModel<BrandModel> pagination = BrandData.createPaginationModel();

        when(brandPersistencePort.findAllPaged(page, size, sortDir, search))
                .thenReturn(Mono.just(pagination));

        StepVerifier.create(brandUseCase.getBrandsPaged(page, size, sortDir, search))
                .expectNextMatches(result ->
                        result.equals(pagination))
                .verifyComplete();

        verify(brandPersistencePort).findAllPaged(page, size, sortDir, search);
    }



    @Test
    void getBrandById_WhenBrandDoesNotExist_ShouldReturnNotFoundException() {
        when(brandPersistencePort.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(brandUseCase.getBrandById(1L))
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException &&
                        throwable.getMessage().equals(BRAND_NOT_FOUND))
                .verify();

        verify(brandPersistencePort).findById(1L);
    }

    @Test
    void updateBrand_WhenBrandExists_ShouldUpdateBrand() {
        BrandModel updatedBrand = BrandData.createBrandModel();
        updatedBrand.setName("UpdatedBrand");

        when(brandPersistencePort.findById(1L)).thenReturn(Mono.just(brandModel));
        when(brandPersistencePort.save(any(BrandModel.class))).thenReturn(Mono.just(updatedBrand));

        StepVerifier.create(brandUseCase.updateBrand(1L, updatedBrand))
                .verifyComplete();

        ArgumentCaptor<BrandModel> captor = ArgumentCaptor.forClass(BrandModel.class);
        verify(brandPersistencePort).save(captor.capture());

        BrandModel savedBrand = captor.getValue();
        assertEquals(1L, savedBrand.getId());
        assertEquals("UpdatedBrand", savedBrand.getName());

        verify(brandPersistencePort).findById(1L);
        verify(brandPersistencePort).save(any(BrandModel.class));
    }

    @Test
    void updateBrand_WhenBrandDoesNotExist_ShouldReturnNotFoundException() {
        BrandModel updatedBrand = BrandData.createBrandModel();
        updatedBrand.setName("UpdatedBrand");

        when(brandPersistencePort.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(brandUseCase.updateBrand(1L, updatedBrand))
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException &&
                        throwable.getMessage().equals(BRAND_NOT_FOUND))
                .verify();

        verify(brandPersistencePort).findById(1L);
        verify(brandPersistencePort, never()).save(any());
    }

}
