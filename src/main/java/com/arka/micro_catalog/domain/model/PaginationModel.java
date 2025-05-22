package com.arka.micro_catalog.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginationModel<T> {

    private List<T> items;
    private long totalElements;
    private int currentPage;
    private int totalPages;

}
