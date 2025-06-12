package com.arka.micro_catalog.adapters.driven.r2dbc.adapter;

import com.arka.micro_catalog.adapters.driven.r2dbc.entity.BrandEntity;
import com.arka.micro_catalog.adapters.driven.r2dbc.mapper.IBrandEntityMapper;
import com.arka.micro_catalog.adapters.driven.r2dbc.repository.IBrandRepository;
import com.arka.micro_catalog.domain.model.BrandModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class BrandAdapterTest {

    @Mock
    private IBrandRepository brandRepository;

    @Mock
    private IBrandEntityMapper brandEntityMapper;

    @InjectMocks
    private BrandAdapter brandAdapter;

    @Test
    void save_ShouldMapEntityAndReturnModel() {
        BrandModel model = new BrandModel(1L, "ACME", "Test brand");
        BrandEntity entity = new BrandEntity(1L, "ACME", "Test brand");

        when(brandEntityMapper.toEntity(model)).thenReturn(entity);
        when(brandRepository.save(entity)).thenReturn(Mono.just(entity));
        when(brandEntityMapper.toModel(entity)).thenReturn(model);

        StepVerifier.create(brandAdapter.save(model))
                .expectNextMatches(result -> result.getName().equals("ACME"))
                .verifyComplete();

        verify(brandEntityMapper).toEntity(model);
        verify(brandRepository).save(entity);
        verify(brandEntityMapper).toModel(entity);
    }

    @Test
    void findByName_ShouldReturnModel() {
        String name = "Nike";
        BrandEntity entity = new BrandEntity(2L, name, "Deporte");
        BrandModel model = new BrandModel(2L, name, "Deporte");

        when(brandRepository.findByName(name)).thenReturn(Mono.just(entity));
        when(brandEntityMapper.toModel(entity)).thenReturn(model);

        StepVerifier.create(brandAdapter.findByName(name))
                .expectNext(model)
                .verifyComplete();

        verify(brandRepository).findByName(name);
        verify(brandEntityMapper).toModel(entity);
    }

    @Test
    void findById_ShouldReturnModel() {
        Long id = 3L;
        BrandEntity entity = new BrandEntity(id, "Apple", "Tech");
        BrandModel model = new BrandModel(id, "Apple", "Tech");

        when(brandRepository.findById(id)).thenReturn(Mono.just(entity));
        when(brandEntityMapper.toModel(entity)).thenReturn(model);

        StepVerifier.create(brandAdapter.findById(id))
                .expectNext(model)
                .verifyComplete();

        verify(brandRepository).findById(id);
        verify(brandEntityMapper).toModel(entity);
    }


}
