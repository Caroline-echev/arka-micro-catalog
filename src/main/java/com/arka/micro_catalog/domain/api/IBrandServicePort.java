package com.arka.micro_catalog.domain.api;

import com.arka.micro_catalog.domain.model.BrandModel;
import reactor.core.publisher.Mono;

public interface IBrandServicePort {

    Mono<Void> createBrand(BrandModel request);
}
