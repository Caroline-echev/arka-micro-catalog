package com.arka.micro_catalog.data;

import com.arka.micro_catalog.adapters.driving.reactive.dto.request.ProductRequest;
import com.arka.micro_catalog.domain.model.ProductModel;

import java.math.BigDecimal;
import java.util.List;

public class ProductData {

    public static ProductRequest createProductRequest() {
        return new ProductRequest("Name", "Product Test", new BigDecimal("1.00"), "ACTIVE", "PHOTO", 1L, List.of(1L));
    }
    public static ProductModel createProductModel() {
        return new ProductModel(1L, "Name", "Product Test", new BigDecimal("1.00"), "ACTIVE", "PHOTO", BrandData.createBrandModel(), List.of(CategoryData.createCategory()));
    }
}
