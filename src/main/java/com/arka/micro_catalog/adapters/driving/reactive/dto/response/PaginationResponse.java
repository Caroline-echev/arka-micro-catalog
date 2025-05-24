package com.arka.micro_catalog.adapters.driving.reactive.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse<T> {

    private List<T> items;
    private long totalElements;
    private int currentPage;
    private int totalPages;
}
