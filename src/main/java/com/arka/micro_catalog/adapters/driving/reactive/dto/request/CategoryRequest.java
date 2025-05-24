package com.arka.micro_catalog.adapters.driving.reactive.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryRequest {

    @NotBlank(message = "Name cannot be blank")
    @Size( max = 100, message = "Name cannot be longer than 100 characters")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    private String description;

}
