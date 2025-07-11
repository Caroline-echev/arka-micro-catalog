package com.arka.micro_catalog.adapters.driving.reactive.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BrandResponse {
    private Long id;
    private String name;
    private String description;
}
