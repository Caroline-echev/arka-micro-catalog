package com.arka.micro_catalog.adapters.driving.reactive.controller;

import com.arka.micro_catalog.adapters.driving.reactive.dto.request.ProductRequest;
import com.arka.micro_catalog.adapters.driving.reactive.mapper.IProductDtoMapper;
import com.arka.micro_catalog.domain.api.IProductServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product Controller", description = "Product Controller Operations")
public class ProductController {

    private final IProductServicePort productServicePort;
    private final IProductDtoMapper productDtoMapper;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new product")
    public Mono<Void> createProduct(@Valid @RequestBody ProductRequest request) {
        return productServicePort.createProduct(productDtoMapper.toModel(request), request.getBrandId() , request.getCategoryIds());
    }

}
