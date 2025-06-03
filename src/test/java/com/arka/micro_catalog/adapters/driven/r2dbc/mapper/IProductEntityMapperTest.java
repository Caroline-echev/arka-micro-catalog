package com.arka.micro_catalog.adapters.driven.r2dbc.mapper;

import com.arka.micro_catalog.domain.model.BrandModel;
import com.arka.micro_catalog.domain.model.ProductModel;
import com.arka.micro_catalog.infrastructure.out.r2dbc.entity.ProductEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class IProductEntityMapperTest {

    private final IProductEntityMapper mapper = new IProductEntityMapper() {
        @Override
        public ProductEntity toEntity(ProductModel model) {
            return new ProductEntity(
                    model.getId(),
                    model.getName(),
                    model.getDescription(),
                    model.getPrice(),
                    model.getStatus(),
                    model.getPhoto(),
                    model.getBrand() != null ? model.getBrand().getId() : null
            );
        }

        @Override
        public ProductModel toModel(ProductEntity entity) {
            BrandModel brand = new BrandModel(entity.getBrandId(), null, null);
            return new ProductModel(
                    entity.getId(),
                    entity.getName(),
                    entity.getDescription(),
                    entity.getPrice(),
                    entity.getStatus(),
                    entity.getPhoto(),
                    brand,
                    null
            );
        }
    };

    @Test
    void toEntity_shouldMapModelToEntity() {
        BrandModel brand = new BrandModel(10L, "Brand Name", "Brand Desc");
        ProductModel model = new ProductModel(
                1L,
                "Product Name",
                "Product Description",
                new BigDecimal("99.99"),
                "ACTIVE",
                "photo.jpg",
                brand,
                null
        );

        ProductEntity entity = mapper.toEntity(model);

        assertNotNull(entity);
        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getName(), entity.getName());
        assertEquals(model.getDescription(), entity.getDescription());
        assertEquals(model.getPrice(), entity.getPrice());
        assertEquals(model.getStatus(), entity.getStatus());
        assertEquals(model.getPhoto(), entity.getPhoto());
        assertEquals(brand.getId(), entity.getBrandId());
    }

    @Test
    void toModel_shouldMapEntityToModel() {
        ProductEntity entity = new ProductEntity(
                1L,
                "Product Name",
                "Product Description",
                new BigDecimal("99.99"),
                "ACTIVE",
                "photo.jpg",
                10L
        );

        ProductModel model = mapper.toModel(entity);

        assertNotNull(model);
        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getName(), model.getName());
        assertEquals(entity.getDescription(), model.getDescription());
        assertEquals(entity.getPrice(), model.getPrice());
        assertEquals(entity.getStatus(), model.getStatus());
        assertEquals(entity.getPhoto(), model.getPhoto());
        assertNotNull(model.getBrand());
        assertEquals(entity.getBrandId(), model.getBrand().getId());
    }
}
