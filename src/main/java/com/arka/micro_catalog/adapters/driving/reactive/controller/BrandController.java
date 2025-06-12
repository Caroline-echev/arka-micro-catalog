package com.arka.micro_catalog.adapters.driving.reactive.controller;


import com.arka.micro_catalog.adapters.driving.reactive.dto.request.BrandRequest;
import com.arka.micro_catalog.adapters.driving.reactive.dto.response.BrandResponse;
import com.arka.micro_catalog.adapters.driving.reactive.dto.response.PaginationResponse;
import com.arka.micro_catalog.adapters.driving.reactive.mapper.IBrandDtoMapper;
import com.arka.micro_catalog.domain.api.IBrandServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static com.arka.micro_catalog.adapters.util.CategoryConstantsDriving.*;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
@Tag(name = "Brand Controller", description = "Brand Controller Operations")
public class BrandController {

    private final IBrandServicePort brandServicePort;
    private final IBrandDtoMapper brandDtoMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new brand")
    public Mono<Void> createBrand(@Valid @RequestBody BrandRequest request) {
        return brandServicePort.createBrand(brandDtoMapper.toModel(request));
    }
    @GetMapping
    @Operation(summary = "Get paginated and filtered brands")
    public Mono<PaginationResponse<BrandResponse>> getBrands(
            @RequestParam(defaultValue = PAGE_DEFAULT) int page,
            @RequestParam(defaultValue = SIZE_DEFAULT) int size,
            @RequestParam(defaultValue = SORT_DEFAULT) String sortDir,
            @RequestParam(required = false) String search) {

        return brandServicePort.getBrandsPaged(page, size, sortDir, search)
                .flatMap(brandDtoMapper::toResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get brand by ID")
    public Mono<BrandResponse> getBrandById(@PathVariable Long id) {
        return brandServicePort.getBrandById(id)
                .map(brandDtoMapper::toResponse);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing brand")
    public Mono<Void> updateBrand(@PathVariable Long id, @Valid @RequestBody BrandRequest request) {
        return brandServicePort.updateBrand(id, brandDtoMapper.toModel(request));
    }

}