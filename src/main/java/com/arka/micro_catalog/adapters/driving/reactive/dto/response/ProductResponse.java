package com.arka.micro_catalog.adapters.driving.reactive.dto.response;

import com.arka.micro_catalog.domain.model.BrandModel;
import com.arka.micro_catalog.domain.model.CategoryModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String status;
    private String photo;
    private BrandModel brand;
    private List<CategoryModel> categories;
}
