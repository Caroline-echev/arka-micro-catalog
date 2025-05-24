package com.arka.micro_catalog.domain.api;

import com.arka.micro_catalog.domain.model.BrandModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
import reactor.core.publisher.Mono;

public interface IBrandServicePort {

    Mono<Void> createBrand(BrandModel request);

    Mono<PaginationModel<BrandModel>> getBrandsPaged(int page, int size, String sortDir, String search);

    Mono<BrandModel> getBrandById(Long id);
}
