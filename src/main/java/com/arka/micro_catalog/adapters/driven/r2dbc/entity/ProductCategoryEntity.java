package com.arka.micro_catalog.infrastructure.out.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("tb_product_categories")
public class ProductCategoryEntity {

    @Id
    private Long id;

    private Long productId;
    private Long categoryId;
}
