package com.arka.micro_catalog.adapters.driven.r2dbc.mapper;

import com.arka.micro_catalog.adapters.driven.r2dbc.entity.BrandEntity;
import com.arka.micro_catalog.domain.model.BrandModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IBrandEntityMapperTest {

    private final IBrandEntityMapper mapper = new IBrandEntityMapper() {
        @Override
        public BrandEntity toEntity(BrandModel model) {
            return new BrandEntity(model.getId(), model.getName(), model.getDescription());
        }

        @Override
        public BrandModel toModel(BrandEntity entity) {
            return new BrandModel(entity.getId(), entity.getName(), entity.getDescription());
        }
    };

    @Test
    void toEntity_shouldMapModelToEntity() {
        BrandModel model = new BrandModel(1L, "Test Brand", "Test Description");

        BrandEntity entity = mapper.toEntity(model);

        assertNotNull(entity);
        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getName(), entity.getName());
        assertEquals(model.getDescription(), entity.getDescription());
    }

    @Test
    void toModel_shouldMapEntityToModel() {
        BrandEntity entity = new BrandEntity(1L, "Test Brand", "Test Description");

        BrandModel model = mapper.toModel(entity);

        assertNotNull(model);
        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getName(), model.getName());
        assertEquals(entity.getDescription(), model.getDescription());
    }
}
