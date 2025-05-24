package com.arka.micro_catalog.domain.usecase;

import com.arka.micro_catalog.data.CategoryData;
import com.arka.micro_catalog.domain.exception.DuplicateResourceException;
import com.arka.micro_catalog.domain.exception.NotFoundException;
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

import static com.arka.micro_catalog.domain.util.constants.CategoryConstants.CATEGORY_ALREADY_EXISTS;
import static com.arka.micro_catalog.domain.util.constants.CategoryConstants.CATEGORY_NOT_FOUND;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryUseCaseTest {

    @Mock
    private ICategoryPersistencePort categoryPersistencePort;

    @InjectMocks
    private CategoryUseCase categoryUseCase;

    private CategoryModel category;

    @BeforeEach
    void setUp() {
        category = CategoryData.createCategory();
    }

    @Test
    void createCategory_success() {
        when(categoryPersistencePort.findByName("Electronics")).thenReturn(Mono.empty());
        when(categoryPersistencePort.save(category)).thenReturn(Mono.just(category));

        StepVerifier.create(categoryUseCase.createCategory(category))
                .verifyComplete();

        verify(categoryPersistencePort).save(category);
    }



    @Test
    void getCategoriesPaged_success() {
        PaginationModel<CategoryModel> paged = CategoryData.createPaginationModel();
        when(categoryPersistencePort.findAllPaged(0, 10, "asc", "")).thenReturn(Mono.just(paged));

        StepVerifier.create(categoryUseCase.getCategoriesPaged(0, 10, "asc", ""))
                .expectNext(paged)
                .verifyComplete();
    }

    @Test
    void getCategoryById_success() {
        when(categoryPersistencePort.findById(1L)).thenReturn(Mono.just(category));

        StepVerifier.create(categoryUseCase.getCategoryById(1L))
                .expectNext(category)
                .verifyComplete();
    }

    @Test
    void getCategoryById_notFound() {
        when(categoryPersistencePort.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(categoryUseCase.getCategoryById(1L))
                .expectErrorMatches(error -> error instanceof NotFoundException &&
                        error.getMessage().equals(CATEGORY_NOT_FOUND))
                .verify();
    }

    @Test
    void updateCategory_success() {
        when(categoryPersistencePort.findById(1L)).thenReturn(Mono.just(category));
        when(categoryPersistencePort.save(category)).thenReturn(Mono.just(category));

        StepVerifier.create(categoryUseCase.updateCategory(1L, category))
                .verifyComplete();

        verify(categoryPersistencePort).save(category);
    }

    @Test
    void updateCategory_notFound() {
        when(categoryPersistencePort.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(categoryUseCase.updateCategory(1L, category))
                .expectErrorMatches(error -> error instanceof NotFoundException &&
                        error.getMessage().equals(CATEGORY_NOT_FOUND))
                .verify();
    }
}
