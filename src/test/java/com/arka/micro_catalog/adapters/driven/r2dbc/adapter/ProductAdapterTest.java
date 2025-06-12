package com.arka.micro_catalog.adapters.driven.r2dbc.adapter;

import com.arka.micro_catalog.adapters.driven.r2dbc.mapper.IProductEntityMapper;
import com.arka.micro_catalog.adapters.driven.r2dbc.repository.IProductRepository;
import com.arka.micro_catalog.data.ProductData;
import com.arka.micro_catalog.domain.model.BrandModel;
import com.arka.micro_catalog.domain.model.ProductModel;
import com.arka.micro_catalog.domain.spi.IBrandPersistencePort;
import com.arka.micro_catalog.domain.spi.ICategoryPersistencePort;
import com.arka.micro_catalog.domain.spi.IProductCategoryPersistencePort;
import com.arka.micro_catalog.infrastructure.out.r2dbc.entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;

class ProductAdapterTest {

    private IProductRepository productRepository;
    private IProductEntityMapper productEntityMapper;
    private IBrandPersistencePort brandPersistencePort;
    private ICategoryPersistencePort categoryPersistencePort;
    private IProductCategoryPersistencePort productCategoryPersistencePort;
    private ProductAdapter productAdapter;

    @BeforeEach
    void setUp() {
        productRepository = mock(IProductRepository.class);
        productEntityMapper = mock(IProductEntityMapper.class);
        brandPersistencePort = mock(IBrandPersistencePort.class);
        categoryPersistencePort = mock(ICategoryPersistencePort.class);
        productCategoryPersistencePort = mock(IProductCategoryPersistencePort.class);

        productAdapter = new ProductAdapter(
                productRepository,
                productEntityMapper,
                brandPersistencePort,
                categoryPersistencePort,
                productCategoryPersistencePort
        );
    }


    @Test
    void findByName_shouldReturnProductModel() {
        String productName = "TestProduct";

        ProductEntity entity = new ProductEntity(5L, productName, "desc", new BigDecimal("5.00"), "ACTIVE", null, 10L);
        ProductModel expectedModel = ProductData.createProductModel();

        when(productRepository.findByName(productName)).thenReturn(Mono.just(entity));
        when(productEntityMapper.toModel(entity)).thenReturn(expectedModel);

        StepVerifier.create(productAdapter.findByName(productName))
                .expectNext(expectedModel)
                .verifyComplete();

        verify(productRepository).findByName(productName);
        verify(productEntityMapper).toModel(entity);
    }



    @Test
    void countWithSearch_shouldReturnCount() {
        String search = "test";

        when(productRepository.countWithSearch(search)).thenReturn(Mono.just(5L));

        StepVerifier.create(productAdapter.countWithSearch(search))
                .expectNext(5L)
                .verifyComplete();

        verify(productRepository).countWithSearch(search);
    }

    @Test
    void findById_shouldAssignBrandModelWhenBrandIdPresent() {
        ProductEntity entity = new ProductEntity(3L, "Name", "Desc", new BigDecimal("12.00"), "ACTIVE", null, 99L);
        ProductModel modelFromMapper =ProductData.createProductModel();

        when(productRepository.findById(3L)).thenReturn(Mono.just(entity));
        when(productEntityMapper.toModel(entity)).thenReturn(modelFromMapper);

        StepVerifier.create(productAdapter.findById(3L))
                .expectNextMatches(productModel ->
                        productModel.getBrand() != null &&
                                productModel.getBrand().getId().equals(99L))
                .verifyComplete();

        verify(productRepository).findById(3L);
        verify(productEntityMapper).toModel(entity);
    }

    @Test
    void save_shouldMapAndReturnProductModel() {
        ProductModel inputModel = new ProductModel(
                1L,
                "Product Name",
                "Description",
                new BigDecimal("9.99"),
                "ACTIVE",
                "photo.png",
                new BrandModel(1L, "Brand", "Desc"),
                List.of()
        );

        ProductEntity entity = new ProductEntity(
                1L, "Product Name", "Description",
                new BigDecimal("9.99"), "ACTIVE", "photo.png", 1L
        );

        when(productEntityMapper.toEntity(inputModel)).thenReturn(entity);
        when(productRepository.save(entity)).thenReturn(Mono.just(entity));
        when(productEntityMapper.toModel(entity)).thenReturn(inputModel);

        StepVerifier.create(productAdapter.save(inputModel))
                .expectNextMatches(saved -> saved.getName().equals("Product Name") &&
                        saved.getBrand().getId().equals(1L))
                .verifyComplete();

        verify(productRepository).save(entity);
        verify(productEntityMapper).toEntity(inputModel);
        verify(productEntityMapper).toModel(entity);
    }

    @Test
    void findById_shouldReturnProductModelWithBrand() {
        ProductEntity entity = new ProductEntity(
                2L, "Product X", "Test", new BigDecimal("10.00"), "ACTIVE", null, 99L
        );

        ProductModel expectedModel = ProductData.createProductModel();
        expectedModel.setId(2L);
        expectedModel.setName("Product X");

        when(productRepository.findById(2L)).thenReturn(Mono.just(entity));
        when(productEntityMapper.toModel(entity)).thenReturn(expectedModel);

        StepVerifier.create(productAdapter.findById(2L))
                .expectNextMatches(model ->
                        model.getId().equals(2L) &&
                                model.getBrand() != null &&
                                model.getBrand().getId().equals(99L)
                )
                .verifyComplete();

        verify(productRepository).findById(2L);
        verify(productEntityMapper).toModel(entity);
    }
}
