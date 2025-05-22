package com.arka.micro_catalog.adapters.driving.reactive.dto.response;

import lombok.Data;

import java.util.List;
@Data
public class PaginationResponse<T> {

    private List<T> items;
    private long totalElements;
    private int currentPage;
    private int totalPages;
}
