package com.arka.micro_catalog.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BrandModel {

    private Long id;
    private String name;
    private String description;
}
