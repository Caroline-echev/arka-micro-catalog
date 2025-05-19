package com.arka.micro_catalog.adapters.driving.reactive.controller;


import com.arka.micro_catalog.adapters.driving.reactive.dto.request.CategoryRequest;
import com.arka.micro_catalog.adapters.driving.reactive.mapper.ICategoryDtoMapper;
import com.arka.micro_catalog.domain.api.ICategoryServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Category Controller", description = "Category Controller Operations")
public class CategoryController {

    private final ICategoryServicePort categoryServicePort;
    private final ICategoryDtoMapper categoryDtoMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new category")
    public Mono<Void> createCategory(@Valid @RequestBody CategoryRequest request) {
        return categoryServicePort.createCategory(categoryDtoMapper.toModel(request));
    }


}

