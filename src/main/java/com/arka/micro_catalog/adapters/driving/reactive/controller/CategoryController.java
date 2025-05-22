package com.arka.micro_catalog.adapters.driving.reactive.controller;


import com.arka.micro_catalog.adapters.driving.reactive.dto.request.CategoryRequest;
import com.arka.micro_catalog.adapters.driving.reactive.dto.response.CategoryResponse;
import com.arka.micro_catalog.adapters.driving.reactive.dto.response.PaginationResponse;
import com.arka.micro_catalog.adapters.driving.reactive.mapper.ICategoryDtoMapper;
import com.arka.micro_catalog.domain.api.ICategoryServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static com.arka.micro_catalog.adapters.util.CategoryConstants.*;

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

    @GetMapping
    @Operation(summary = "Get paginated and filtered categories")
    public Mono<PaginationResponse<CategoryResponse>> getCategories(
            @RequestParam(defaultValue = PAGE_DEFAULT) int page,
            @RequestParam(defaultValue = SIZE_DEFAULT) int size,
            @RequestParam(defaultValue = SORT_DEFAULT) String sortDir,
            @RequestParam(required = false) String search) {

        return categoryServicePort.getCategoriesPaged(page, size, sortDir, search)
                .flatMap(categoryDtoMapper::toResponse);
    }
}