package com.arka.micro_catalog.domain.usecase;


import com.arka.micro_catalog.domain.api.IProductServicePort;
import com.arka.micro_catalog.domain.exception.DuplicateResourceException;
import com.arka.micro_catalog.domain.model.BrandModel;
import com.arka.micro_catalog.domain.model.CategoryModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
import com.arka.micro_catalog.domain.model.ProductModel;
import com.arka.micro_catalog.domain.spi.IBrandPersistencePort;
import com.arka.micro_catalog.domain.spi.ICategoryPersistencePort;
import com.arka.micro_catalog.domain.spi.IProductCategoryPersistencePort;
import com.arka.micro_catalog.domain.spi.IProductPersistencePort;
import com.arka.micro_catalog.domain.util.validation.ProductValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.arka.micro_catalog.domain.util.constants.ProductConstants.ERROR_PRODUCT_ALREADY_EXISTS;


@Service
@RequiredArgsConstructor
public class ProductUseCase implements IProductServicePort {
    private final IProductPersistencePort productPersistencePort;
    private final ICategoryPersistencePort categoryPersistencePort;
    private final IBrandPersistencePort brandPersistencePort;
    private final IProductCategoryPersistencePort productCategoryPersistencePort;


    @Override
    public Mono<Void> createProduct(ProductModel productModel, Long brandId, List<Long> categoryIds) {
        return checkProductExists(productModel.getName())
                .then(ProductValidationUtil.validateBrandExists(brandId, brandPersistencePort)
                        .flatMap(brand -> ProductValidationUtil.validateCategoriesExist(categoryIds, categoryPersistencePort)
                                .flatMap(categories -> {
                                    productModel.setBrand(brand);
                                    productModel.setCategories(categories);

                                    return productPersistencePort.save(productModel)
                                            .flatMap(savedProduct ->
                                                    productCategoryPersistencePort.saveProductCategories(
                                                            savedProduct.getId(),
                                                            categoryIds
                                                    )
                                            );
                                })));
    }

    @Override
    public Mono<PaginationModel<ProductModel>> getProducts(int page, int size, String sortDir, String search) {
        Flux<ProductModel> modelFlux = productPersistencePort.findAllPagedRaw(page, size, sortDir, search)
                .flatMap(product -> {
                  Mono<BrandModel> brandMono;
                    if (product.getBrand() != null && product.getBrand().getId() != null) {
                        brandMono = brandPersistencePort.findById(product.getBrand().getId());
                    } else {
                          brandMono = Mono.empty();
                    }

                    Flux<Long> categoryIdsFlux = productCategoryPersistencePort.findCategoryIdsByProductId(product.getId());
                    Mono<List<CategoryModel>> categoriesMono = categoryIdsFlux
                            .flatMap(categoryPersistencePort::findById)
                            .collectList();

                    return Mono.zip(Mono.just(product), brandMono, categoriesMono)
                            .map(tuple -> {
                                ProductModel enriched = tuple.getT1();
                                enriched.setBrand(tuple.getT2());
                                enriched.setCategories(tuple.getT3());
                                return enriched;
                            });
                });

        Mono<List<ProductModel>> itemsMono = modelFlux.collectList();
        Mono<Long> countMono = productPersistencePort.countWithSearch(search);

        return Mono.zip(itemsMono, countMono)
                .map(tuple -> {
                    List<ProductModel> items = tuple.getT1();
                    long totalElements = tuple.getT2();
                    int totalPages = (int) Math.ceil((double) totalElements / size);

                    return PaginationModel.<ProductModel>builder()
                            .items(items)
                            .totalElements(totalElements)
                            .currentPage(page)
                            .totalPages(totalPages)
                            .build();
                });
    }


    private Mono<Void> checkProductExists(String productName) {
        return productPersistencePort.findByName(productName)
                .flatMap(existing -> Mono.error(new DuplicateResourceException(ERROR_PRODUCT_ALREADY_EXISTS)))
                .then();
    }
}
