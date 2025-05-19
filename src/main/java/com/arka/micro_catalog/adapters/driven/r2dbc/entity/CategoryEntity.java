package com.arka.micro_catalog.adapters.driven.r2dbc.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("tb_category")
public class CategoryEntity {

    @Id
    @Column("id")
    private Long id;

    @Column("name")
    private String name;

    @Column("description")
    private String description;

}
