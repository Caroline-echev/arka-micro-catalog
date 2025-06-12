package com.arka.micro_catalog.adapters.driven.r2dbc.mapper;

import com.arka.micro_catalog.adapters.driven.r2dbc.entity.CategoryEntity;
import com.arka.micro_catalog.domain.model.CategoryModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ICategoryEntityMapperTest {

    private final ICategoryEntityMapper mapper = new ICategoryEntityMapper() {
        @Override
        public CategoryEntity toEntity(CategoryModel model) {
            return new CategoryEntity(model.getId(), model.getName(), model.getDescription());
        }

        @Override
        public CategoryModel toModel(CategoryEntity entity) {
            return new CategoryModel(entity.getId(), entity.getName(), entity.getDescription());
        }
    };

    @Test
    void toEntity_shouldMapModelToEntity() {
        CategoryModel model = new CategoryModel(1L, "Test Category", "Test Description");

        CategoryEntity entity = mapper.toEntity(model);

        assertNotNull(entity);
        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getName(), entity.getName());
        assertEquals(model.getDescription(), entity.getDescription());
    }

    @Test
    void toModel_shouldMapEntityToModel() {
        CategoryEntity entity = new CategoryEntity(1L, "Test Category", "Test Description");

        CategoryModel model = mapper.toModel(entity);

        assertNotNull(model);
        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getName(), model.getName());
        assertEquals(entity.getDescription(), model.getDescription());
    }
}
