package com.arka.micro_catalog.adapters.driven.r2dbc.adapter;

import com.arka.micro_catalog.adapters.driven.r2dbc.adapter.CategoryAdapter;
import com.arka.micro_catalog.adapters.driven.r2dbc.entity.CategoryEntity;
import com.arka.micro_catalog.adapters.driven.r2dbc.mapper.ICategoryEntityMapper;
import com.arka.micro_catalog.adapters.driven.r2dbc.repository.ICategoryRepository;
import com.arka.micro_catalog.data.CategoryData;
import com.arka.micro_catalog.domain.model.CategoryModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryAdapterTest {

    @Mock
    private ICategoryRepository categoryRepository;

    @Mock
    private ICategoryEntityMapper categoryEntityMapper;

    @InjectMocks
    private CategoryAdapter categoryAdapter;

    private CategoryEntity entity;
    private CategoryModel model;

    @BeforeEach
    void setUp() {
        entity = CategoryData.createCategoryEntity();
        model = CategoryData.createCategory();
    }

    @Test
    void save_shouldReturnSavedModel() {
        when(categoryEntityMapper.toEntity(model)).thenReturn(entity);
        when(categoryRepository.save(entity)).thenReturn(Mono.just(entity));
        when(categoryEntityMapper.toModel(entity)).thenReturn(model);

        StepVerifier.create(categoryAdapter.save(model))
                .expectNext(model)
                .verifyComplete();

        verify(categoryRepository).save(entity);
    }

    @Test
    void findByName_shouldReturnModel() {
        when(categoryRepository.findByName("Hardware")).thenReturn(Mono.just(entity));
        when(categoryEntityMapper.toModel(entity)).thenReturn(model);

        StepVerifier.create(categoryAdapter.findByName("Hardware"))
                .expectNext(model)
                .verifyComplete();
    }

    @Test
    void findById_shouldReturnModel() {
        when(categoryRepository.findById(1L)).thenReturn(Mono.just(entity));
        when(categoryEntityMapper.toModel(entity)).thenReturn(model);

        StepVerifier.create(categoryAdapter.findById(1L))
                .expectNext(model)
                .verifyComplete();
    }

    @Test
    void findAllPaged_shouldReturnPaginationModel() {
        List<CategoryEntity> entityList = List.of(entity);
        List<CategoryModel> modelList = List.of(model);

        when(categoryRepository.findAllPagedWithSearch(null, "asc", 10, 0L)).thenReturn(Flux.fromIterable(entityList));
        when(categoryEntityMapper.toModel(entity)).thenReturn(model);
        when(categoryRepository.countWithSearch(null)).thenReturn(Mono.just(1L));

        StepVerifier.create(categoryAdapter.findAllPaged(0, 10, "asc", null))
                .expectNextMatches(pagination -> {
                    return pagination.getItems().size() == 1 &&
                            pagination.getTotalElements() == 1 &&
                            pagination.getCurrentPage() == 0 &&
                            pagination.getTotalPages() == 1;
                })
                .verifyComplete();
    }
}
