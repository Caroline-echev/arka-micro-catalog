package com.arka.micro_catalog.adapters.driven.r2dbc.adapter;

import com.arka.micro_catalog.adapters.driven.r2dbc.repository.IProductCategoryRepository;
import com.arka.micro_catalog.infrastructure.out.r2dbc.entity.ProductCategoryEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductCategoryAdapterTest {

    @Mock
    private IProductCategoryRepository productCategoryRepository;

    @InjectMocks
    private ProductCategoryAdapter productCategoryAdapter;

    @Test
    void saveProductCategories_ShouldSaveAllCategoriesAndComplete() {
        Long productId = 1L;
        List<Long> categoryIds = List.of(10L, 20L, 30L);

        when(productCategoryRepository.save(any(ProductCategoryEntity.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(productCategoryAdapter.saveProductCategories(productId, categoryIds))
                .verifyComplete();

        ArgumentCaptor<ProductCategoryEntity> captor = ArgumentCaptor.forClass(ProductCategoryEntity.class);
        verify(productCategoryRepository, times(categoryIds.size())).save(captor.capture());

        List<ProductCategoryEntity> savedEntities = captor.getAllValues();
        for (int i = 0; i < categoryIds.size(); i++) {
            assertEquals(productId, savedEntities.get(i).getProductId());
            assertEquals(categoryIds.get(i), savedEntities.get(i).getCategoryId());
        }
    }

    @Test
    void findCategoryIdsByProductId_ShouldReturnCategoryIdsFlux() {
        Long productId = 1L;

        ProductCategoryEntity e1 = ProductCategoryEntity.builder().productId(productId).categoryId(10L).build();
        ProductCategoryEntity e2 = ProductCategoryEntity.builder().productId(productId).categoryId(20L).build();

        when(productCategoryRepository.findByProductId(productId)).thenReturn(Flux.just(e1, e2));

        StepVerifier.create(productCategoryAdapter.findCategoryIdsByProductId(productId))
                .expectNext(10L, 20L)
                .verifyComplete();

        verify(productCategoryRepository).findByProductId(productId);
    }

    @Test
    void deleteByProductId_ShouldComplete() {
        Long productId = 1L;

        when(productCategoryRepository.deleteByProductId(productId)).thenReturn(Mono.empty());

        StepVerifier.create(productCategoryAdapter.deleteByProductId(productId))
                .verifyComplete();

        verify(productCategoryRepository).deleteByProductId(productId);
    }
}
