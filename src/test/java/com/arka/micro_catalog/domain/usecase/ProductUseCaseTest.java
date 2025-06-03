package com.arka.micro_catalog.domain.usecase;

import com.arka.micro_catalog.data.BrandData;
import com.arka.micro_catalog.data.CategoryData;
import com.arka.micro_catalog.data.ProductData;
import com.arka.micro_catalog.domain.exception.DuplicateResourceException;
import com.arka.micro_catalog.domain.exception.NotFoundException;
import com.arka.micro_catalog.domain.model.BrandModel;
import com.arka.micro_catalog.domain.model.CategoryModel;
import com.arka.micro_catalog.domain.model.ProductModel;
import com.arka.micro_catalog.domain.spi.IBrandPersistencePort;
import com.arka.micro_catalog.domain.spi.ICategoryPersistencePort;
import com.arka.micro_catalog.domain.spi.IProductCategoryPersistencePort;
import com.arka.micro_catalog.domain.spi.IProductPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductUseCaseTest {

    @Mock
    private IProductPersistencePort productPersistencePort;

    @Mock
    private ICategoryPersistencePort categoryPersistencePort;

    @Mock
    private IBrandPersistencePort brandPersistencePort;

    @Mock
    private IProductCategoryPersistencePort productCategoryPersistencePort;

    @InjectMocks
    private ProductUseCase productUseCase;

    private ProductModel productModel;
    private BrandModel brandModel;
    private List<CategoryModel> categories;

    @BeforeEach
    void setUp() {
        productModel = ProductData.createProductModel();
        brandModel = productModel.getBrand();
        categories = productModel.getCategories();
    }



    @Test
    void createProduct_Success() {
        when(productPersistencePort.findByName(anyString())).thenReturn(Mono.empty());
        when(brandPersistencePort.findById(anyLong())).thenReturn(Mono.just(brandModel));
        when(categoryPersistencePort.findAllByIds(anyList())).thenReturn(Flux.fromIterable(categories));
        when(productPersistencePort.save(any())).thenReturn(Mono.just(productModel));
        when(productCategoryPersistencePort.saveProductCategories(anyLong(), anyList())).thenReturn(Mono.empty());

        StepVerifier.create(productUseCase.createProduct(productModel, brandModel.getId(),
                        categories.stream().map(CategoryModel::getId).toList()))
                .verifyComplete();

        verify(productPersistencePort).save(any());
        verify(productCategoryPersistencePort).saveProductCategories(anyLong(), anyList());
    }

    @Test
    void updateProduct_Success() {
        when(productPersistencePort.findById(anyLong())).thenReturn(Mono.just(productModel));
        when(productPersistencePort.findByName(anyString())).thenReturn(Mono.empty());
        when(brandPersistencePort.findById(anyLong())).thenReturn(Mono.just(brandModel));
        when(categoryPersistencePort.findAllByIds(anyList())).thenReturn(Flux.fromIterable(categories));
        when(productPersistencePort.save(any())).thenReturn(Mono.just(productModel));
        when(productCategoryPersistencePort.deleteByProductId(anyLong())).thenReturn(Mono.empty());
        when(productCategoryPersistencePort.saveProductCategories(anyLong(), anyList())).thenReturn(Mono.empty());

        StepVerifier.create(productUseCase.updateProduct(productModel.getId(), productModel, brandModel.getId(),
                        categories.stream().map(CategoryModel::getId).toList()))
                .assertNext(product -> {
                    assert product.getId().equals(productModel.getId());
                    assert product.getName().equals(productModel.getName());
                })
                .verifyComplete();

        verify(productPersistencePort).save(any());
        verify(productCategoryPersistencePort).deleteByProductId(anyLong());
        verify(productCategoryPersistencePort).saveProductCategories(anyLong(), anyList());
    }


    @Test
    void updateProduct_NotFound() {
        when(productPersistencePort.findById(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(productUseCase.updateProduct(99L, productModel, brandModel.getId(), List.of(categories.get(0).getId())))
                .expectError(NotFoundException.class)
                .verify();
    }


    @Test
    void getProducts_ReturnsPagination() {
        when(productPersistencePort.findAllPagedRaw(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(Flux.just(productModel));
        when(productPersistencePort.countWithSearch(anyString())).thenReturn(Mono.just(1L));
        when(brandPersistencePort.findById(anyLong())).thenReturn(Mono.just(brandModel));
        when(productCategoryPersistencePort.findCategoryIdsByProductId(anyLong())).thenReturn(Flux.just(1L));
        when(categoryPersistencePort.findById(anyLong())).thenReturn(Mono.just(categories.get(0)));


        StepVerifier.create(productUseCase.getProducts(0, 10, "asc", ""))
                .assertNext(pagination -> {
                    assert pagination.getItems().size() == 1;
                    assert pagination.getTotalElements() == 1;
                })
                .verifyComplete();
    }



    @Test
    void getProductById_Found() {
        when(productPersistencePort.findById(anyLong())).thenReturn(Mono.just(productModel));
        when(brandPersistencePort.findById(anyLong())).thenReturn(Mono.just(brandModel));
        when(productCategoryPersistencePort.findCategoryIdsByProductId(anyLong())).thenReturn(Flux.just(1L));
        when(categoryPersistencePort.findById(anyLong())).thenReturn(Mono.just(categories.get(0)));


        StepVerifier.create(productUseCase.getProductById(1L))
                .assertNext(product -> {
                    assert product.getId().equals(productModel.getId());
                    assert product.getBrand() != null;
                    assert !product.getCategories().isEmpty();
                })
                .verifyComplete();
    }

    @Test
    void getProductById_NotFound() {
        when(productPersistencePort.findById(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(productUseCase.getProductById(99L))
                .expectError(NotFoundException.class)
                .verify();
    }



}
