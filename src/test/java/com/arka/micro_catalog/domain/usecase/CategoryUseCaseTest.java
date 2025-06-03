package com.arka.micro_catalog.domain.usecase;

import com.arka.micro_catalog.data.CategoryData;
import com.arka.micro_catalog.domain.model.CategoryModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
import com.arka.micro_catalog.domain.spi.ICategoryPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryUseCaseTest {

    @Mock
    private ICategoryPersistencePort categoryPersistencePort;

    @InjectMocks
    private CategoryUseCase categoryUseCase;

    private CategoryModel categoryModel;

    @BeforeEach
    void setUp() {
        categoryModel = CategoryData.createCategory();
    }

    @Test
    void testCreateCategorySuccess() {
        when(categoryPersistencePort.findByName(anyString())).thenReturn(Mono.empty());
        when(categoryPersistencePort.save(any(CategoryModel.class))).thenReturn(Mono.just(categoryModel));

        StepVerifier.create(categoryUseCase.createCategory(categoryModel))
                .verifyComplete();

        verify(categoryPersistencePort).save(categoryModel);
    }

    @Test
    void testGetCategoriesPaged() {
        PaginationModel<CategoryModel> pagination = CategoryData.createPaginationModel();

        when(categoryPersistencePort.findAllPaged(0, 1, "asc", "Peri")).thenReturn(Mono.just(pagination));

        StepVerifier.create(categoryUseCase.getCategoriesPaged(0, 1, "asc", "Peri"))
                .expectNext(pagination)
                .verifyComplete();
    }

    @Test
    void testGetCategoryByIdExists() {
        when(categoryPersistencePort.findById(1L)).thenReturn(Mono.just(categoryModel));

        StepVerifier.create(categoryUseCase.getCategoryById(1L))
                .expectNext(categoryModel)
                .verifyComplete();
    }

    @Test
    void testUpdateCategorySuccess() {
        CategoryModel updatedModel = CategoryData.createCategory();

        when(categoryPersistencePort.findById(1L)).thenReturn(Mono.just(categoryModel));
        when(categoryPersistencePort.findByName("Electronics")).thenReturn(Mono.empty());
        when(categoryPersistencePort.save(any(CategoryModel.class))).thenReturn(Mono.just(updatedModel));

        StepVerifier.create(categoryUseCase.updateCategory(1L, updatedModel))
                .verifyComplete();

        verify(categoryPersistencePort).save(updatedModel);
    }

}
