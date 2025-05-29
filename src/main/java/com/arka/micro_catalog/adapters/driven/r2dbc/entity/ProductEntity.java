package com.arka.micro_catalog.infrastructure.out.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("tb_products")
public class ProductEntity {

    @Id
    private Long id;

    private String name;
    private String description;
    private BigDecimal price;
    private String status;
    private String photo;

    @Column("brand_id")
    private Long brandId;
}
