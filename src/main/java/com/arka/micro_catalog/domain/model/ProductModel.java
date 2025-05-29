package com.arka.micro_catalog.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class ProductModel {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String status;
    private String photo;
    private BrandModel brand;
    private List<CategoryModel> categories;
}
