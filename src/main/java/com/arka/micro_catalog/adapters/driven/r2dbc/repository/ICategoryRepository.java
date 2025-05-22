package com.arka.micro_catalog.adapters.driven.r2dbc.repository;

import com.arka.micro_catalog.adapters.driven.r2dbc.entity.CategoryEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ICategoryRepository extends ReactiveCrudRepository<CategoryEntity, Long> {
    Mono<CategoryEntity> findByName(String name);



    @Query("SELECT * FROM tb_category WHERE (:search IS NULL OR name ILIKE CONCAT('%', :search, '%') OR description ILIKE CONCAT('%', :search, '%')) ORDER BY " +
            "CASE WHEN :sortDir = 'asc' THEN name END ASC, " +
            "CASE WHEN :sortDir = 'desc' THEN name END DESC " +
            "LIMIT :size OFFSET :offset")
    Flux<CategoryEntity> findAllPagedWithSearch(@Param("search") String search,
                                                @Param("sortDir") String sortDir,
                                                @Param("size") int size,
                                                @Param("offset") long offset);

    @Query("SELECT COUNT(*) FROM tb_category WHERE (:search IS NULL OR name ILIKE CONCAT('%', :search, '%') OR description ILIKE CONCAT('%', :search, '%'))")
    Mono<Long> countWithSearch(@Param("search") String search);
}
