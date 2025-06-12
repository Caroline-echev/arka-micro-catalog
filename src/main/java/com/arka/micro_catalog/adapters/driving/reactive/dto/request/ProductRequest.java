package com.arka.micro_catalog.adapters.driving.reactive.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import static com.arka.micro_catalog.adapters.util.ProductConstantsDriving.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    @NotBlank(message = NAME_REQUIRED)
    @Size(max = NAME_MAX_LENGTH, message = NAME_MAX_MESSAGE)
    private String name;

    @NotBlank(message = DESCRIPTION_REQUIRED)
    @Size(max = DESCRIPTION_MAX_LENGTH, message = DESCRIPTION_MAX_MESSAGE)
    private String description;

    @NotNull(message = PRICE_REQUIRED)
    @DecimalMin(value = PRICE_MIN_VALUE, message = PRICE_MIN_MESSAGE)
    private BigDecimal price;

    @NotBlank(message = STATUS_REQUIRED)
    private String status;

    @NotBlank(message = PHOTO_REQUIRED)
    private String photo;

    @NotNull(message = BRAND_ID_REQUIRED)
    private Long brandId;

    @NotEmpty(message = CATEGORY_IDS_REQUIRED)
    @Size(min = CATEGORY_IDS_MIN, max = CATEGORY_IDS_MAX, message = CATEGORY_IDS_SIZE_MESSAGE)
    private List<Long> categoryIds;

}
