package com.arka.micro_catalog.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PaginationModel<T> {

    private List<T> items;
    private long totalElements;
    private int currentPage;
    private int totalPages;

}
