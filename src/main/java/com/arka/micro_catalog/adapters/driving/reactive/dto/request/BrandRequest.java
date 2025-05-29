package com.arka.micro_catalog.adapters.driving.reactive.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import static com.arka.micro_catalog.adapters.util.BrandConstantsDriving.*;
@Data
@AllArgsConstructor
public class BrandRequest {
    @NotBlank(message = NAME_NOT_BLANK)
    @Size( max = MAX_PAGE_SIZE, message =NAME_MAX_SIZE)
    private String name;

    @NotBlank(message =DESCRIPTION_NOT_BLANK)
    private String description;
}
