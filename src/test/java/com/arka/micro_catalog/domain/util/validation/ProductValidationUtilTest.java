package com.arka.micro_catalog.domain.util.validation;

import com.arka.micro_catalog.domain.exception.BadRequestException;
import com.arka.micro_catalog.domain.exception.NotFoundException;
import com.arka.micro_catalog.domain.model.BrandModel;
import com.arka.micro_catalog.domain.model.CategoryModel;
import com.arka.micro_catalog.domain.spi.IBrandPersistencePort;
import com.arka.micro_catalog.domain.spi.ICategoryPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static com.arka.micro_catalog.domain.util.constants.ProductConstants.*;

@ExtendWith(MockitoExtension.class)
class ProductValidationUtilTest {

    @Mock
    IBrandPersistencePort brandPersistencePort;

    @Mock
    ICategoryPersistencePort categoryPersistencePort;


    @Test
    void validateBrandExists_shouldReturnBrand_whenBrandFound() {
        BrandModel brand = new BrandModel(1L, "BrandName", "Description");
        Mockito.when(brandPersistencePort.findById(1L)).thenReturn(Mono.just(brand));

        StepVerifier.create(ProductValidationUtil.validateBrandExists(1L, brandPersistencePort))
                .expectNext(brand)
                .verifyComplete();

        Mockito.verify(brandPersistencePort).findById(1L);
    }

    @Test
    void validateBrandExists_shouldErrorNotFound_whenBrandNotFound() {
        Mockito.when(brandPersistencePort.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(ProductValidationUtil.validateBrandExists(1L, brandPersistencePort))
                .expectErrorMatches(err -> err instanceof NotFoundException &&
                        err.getMessage().equals(ERROR_BRAND_NOT_FOUND))
                .verify();

        Mockito.verify(brandPersistencePort).findById(1L);
    }

    @Test
    void validateCategoriesExist_shouldReturnCategories_whenValid() {
        List<Long> ids = List.of(1L, 2L);
        List<CategoryModel> categories = List.of(
                new CategoryModel(1L, "Cat1", "Desc1"),
                new CategoryModel(2L, "Cat2", "Desc2")
        );

        Mockito.when(categoryPersistencePort.findAllByIds(ids)).thenReturn(Flux.fromIterable(categories));

        StepVerifier.create(ProductValidationUtil.validateCategoriesExist(ids, categoryPersistencePort))
                .expectNext(categories)
                .verifyComplete();

        Mockito.verify(categoryPersistencePort).findAllByIds(ids);
    }

    @Test
    void validateCategoriesExist_shouldErrorBadRequest_whenCategoryListNull() {
        StepVerifier.create(ProductValidationUtil.validateCategoriesExist(null, categoryPersistencePort))
                .expectErrorMatches(err -> err instanceof BadRequestException &&
                        err.getMessage().equals(ERROR_CATEGORY_COUNT_INVALID))
                .verify();

        Mockito.verifyNoInteractions(categoryPersistencePort);
    }

    @Test
    void validateCategoriesExist_shouldErrorBadRequest_whenCategoryListEmpty() {
        StepVerifier.create(ProductValidationUtil.validateCategoriesExist(List.of(), categoryPersistencePort))
                .expectErrorMatches(err -> err instanceof BadRequestException &&
                        err.getMessage().equals(ERROR_CATEGORY_COUNT_INVALID))
                .verify();

        Mockito.verifyNoInteractions(categoryPersistencePort);
    }

    @Test
    void validateCategoriesExist_shouldErrorBadRequest_whenCategoryListTooLarge() {
        List<Long> tooMany = List.of(1L, 2L, 3L, 4L);

        StepVerifier.create(ProductValidationUtil.validateCategoriesExist(tooMany, categoryPersistencePort))
                .expectErrorMatches(err -> err instanceof BadRequestException &&
                        err.getMessage().equals(ERROR_CATEGORY_COUNT_INVALID))
                .verify();

        Mockito.verifyNoInteractions(categoryPersistencePort);
    }

    @Test
    void validateCategoriesExist_shouldErrorBadRequest_whenCategoryListHasDuplicates() {
        List<Long> duplicates = List.of(1L, 1L);

        StepVerifier.create(ProductValidationUtil.validateCategoriesExist(duplicates, categoryPersistencePort))
                .expectErrorMatches(err -> err instanceof BadRequestException &&
                        err.getMessage().equals(ERROR_CATEGORY_DUPLICATE))
                .verify();

        Mockito.verifyNoInteractions(categoryPersistencePort);
    }

    @Test
    void validateCategoriesExist_shouldErrorNotFound_whenCategoriesMissing() {
        List<Long> ids = List.of(1L, 2L);

        List<CategoryModel> found = List.of(new CategoryModel(1L, "Cat1", "Desc1"));
        Mockito.when(categoryPersistencePort.findAllByIds(ids)).thenReturn(Flux.fromIterable(found));

        StepVerifier.create(ProductValidationUtil.validateCategoriesExist(ids, categoryPersistencePort))
                .expectErrorMatches(err -> err instanceof NotFoundException &&
                        err.getMessage().equals(ERROR_CATEGORIES_NOT_FOUND))
                .verify();

        Mockito.verify(categoryPersistencePort).findAllByIds(ids);
    }
}
