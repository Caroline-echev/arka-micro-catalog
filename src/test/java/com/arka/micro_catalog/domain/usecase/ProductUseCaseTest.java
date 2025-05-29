package com.arka.micro_catalog.domain.usecase;

import com.arka.micro_catalog.data.CategoryData;
import com.arka.micro_catalog.data.ProductData;
import com.arka.micro_catalog.domain.exception.DuplicateResourceException;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static com.arka.micro_catalog.domain.util.constants.ProductConstants.ERROR_PRODUCT_ALREADY_EXISTS;
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

    @BeforeEach
    void setUp() {
        productModel = ProductData.createProductModel();
    }
    @Test
    void createProduct_Success() {
        Long brandId = 1L;
        List<Long> categoryIds = List.of(10L, 20L);

        BrandModel brandModel = new BrandModel(brandId, "BrandName", "Description");
        List<CategoryModel> categories = List.of(CategoryData.createCategory(), CategoryData.createCategory());

        when(productPersistencePort.findByName("Name")).thenReturn(Mono.empty());
        when(brandPersistencePort.findById(brandId)).thenReturn(Mono.just(brandModel));
        when(categoryPersistencePort.findAllByIds(categoryIds)).thenReturn(Flux.fromIterable(categories));
        when(productPersistencePort.save(any(ProductModel.class))).thenAnswer(invocation -> {
            ProductModel product = invocation.getArgument(0);
            product.setId(100L);
            return Mono.just(product);
        });
        when(productCategoryPersistencePort.saveProductCategories(100L, categoryIds)).thenReturn(Mono.empty());

        StepVerifier.create(productUseCase.createProduct(productModel, brandId, categoryIds))
                .verifyComplete();

        verify(productPersistencePort).findByName("Name");
        verify(brandPersistencePort).findById(brandId);
        verify(categoryPersistencePort).findAllByIds(categoryIds);
        verify(productPersistencePort).save(any(ProductModel.class));
        verify(productCategoryPersistencePort).saveProductCategories(100L, categoryIds);
    }


}
