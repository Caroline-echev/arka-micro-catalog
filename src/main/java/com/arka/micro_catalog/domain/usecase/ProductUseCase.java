package com.arka.micro_catalog.domain.usecase;

import com.arka.micro_catalog.domain.api.IProductServicePort;
import com.arka.micro_catalog.domain.exception.DuplicateResourceException;
import com.arka.micro_catalog.domain.exception.NotFoundException;
import com.arka.micro_catalog.domain.model.BrandModel;
import com.arka.micro_catalog.domain.model.CategoryModel;
import com.arka.micro_catalog.domain.model.PaginationModel;
import com.arka.micro_catalog.domain.model.ProductModel;
import com.arka.micro_catalog.domain.spi.IBrandPersistencePort;
import com.arka.micro_catalog.domain.spi.ICategoryPersistencePort;
import com.arka.micro_catalog.domain.spi.IProductCategoryPersistencePort;
import com.arka.micro_catalog.domain.spi.IProductPersistencePort;
import com.arka.micro_catalog.domain.util.validation.BrandValidator;
import com.arka.micro_catalog.domain.util.validation.CategoryValidator;
import com.arka.micro_catalog.domain.util.validation.ProductValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

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
        return validateProductCreation(productModel.getName())
                .then(enrichProductWithBrandAndCategories(productModel, brandId, categoryIds))
                .flatMap(this::saveProductWithCategories);
    }

    @Override
    public Mono<PaginationModel<ProductModel>> getProducts(int page, int size, String sortDir, String search) {
        return Mono.zip(
                getEnrichedProducts(page, size, sortDir, search),
                getTotalCount(search)
        ).map(tuple -> buildPaginationModel(tuple.getT1(), tuple.getT2(), page, size));
    }

    @Override
    public Mono<ProductModel> getProductById(Long id) {
        return findProductById(id)
                .flatMap(this::enrichProductWithRelations);
    }

    @Override
    public Mono<ProductModel> updateProduct(Long productId, ProductModel productModel, Long brandId, List<Long> categoryIds) {
        return findProductById(productId)
                .flatMap(existingProduct ->
                        validateProductUpdate(productModel.getName(), productId)
                                .then(enrichProductWithBrandAndCategories(productModel, brandId, categoryIds))
                                .flatMap(enrichedProduct ->
                                        updateExistingProduct(existingProduct, enrichedProduct)
                                                .flatMap(updatedProduct -> saveUpdatedProductWithCategories(updatedProduct, productId, categoryIds))
                                )
                );
    }

    private Mono<Void> validateProductCreation(String productName) {
        return ProductValidator.validateProductDoesNotExistByName(productName, productPersistencePort);
    }

    private Mono<Void> validateProductUpdate(String productName, Long productId) {
        return checkProductNameUniquenessForUpdate(productName, productId);
    }

    private Mono<ProductModel> enrichProductWithBrandAndCategories(ProductModel productModel, Long brandId, List<Long> categoryIds) {
        return Mono.zip(
                getBrandById(brandId),
                getCategoriesByIds(categoryIds)
        ).map(tuple -> {
            productModel.setBrand(tuple.getT1());
            productModel.setCategories(tuple.getT2());
            return productModel;
        });
    }

    private Mono<BrandModel> getBrandById(Long brandId) {
        return BrandValidator.validateBrandExistsById(brandId, brandPersistencePort);
    }

    private Mono<List<CategoryModel>> getCategoriesByIds(List<Long> categoryIds) {
        return CategoryValidator.validateCategoriesExist(categoryIds, categoryPersistencePort);
    }

    private Mono<Void> saveProductWithCategories(ProductModel productModel) {
        return productPersistencePort.save(productModel)
                .flatMap(savedProduct ->
                        saveProductCategoryRelations(savedProduct.getId(), productModel.getCategories())
                );
    }

    private Mono<Void> saveProductCategoryRelations(Long productId, List<CategoryModel> categories) {
        List<Long> categoryIds = categories.stream()
                .map(CategoryModel::getId)
                .toList();
        return productCategoryPersistencePort.saveProductCategories(productId, categoryIds);
    }

    private Mono<List<ProductModel>> getEnrichedProducts(int page, int size, String sortDir, String search) {
        return productPersistencePort.findAllPagedRaw(page, size, sortDir, search)
                .flatMap(this::enrichProductWithRelations)
                .collectList();
    }

    private Mono<Long> getTotalCount(String search) {
        return productPersistencePort.countWithSearch(search);
    }

    private PaginationModel<ProductModel> buildPaginationModel(List<ProductModel> items, Long totalElements, int page, int size) {
        int totalPages = (int) Math.ceil((double) totalElements / size);

        return PaginationModel.<ProductModel>builder()
                .items(items)
                .totalElements(totalElements)
                .currentPage(page)
                .totalPages(totalPages)
                .build();
    }

    private Mono<ProductModel> findProductById(Long id) {
        return productPersistencePort.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Product not found with id: " + id)));
    }

    private Mono<ProductModel> enrichProductWithRelations(ProductModel product) {
        return Mono.zip(
                Mono.just(product),
                getBrandForProduct(product),
                getCategoriesForProduct(product.getId())
        ).map(tuple -> {
            ProductModel enrichedProduct = tuple.getT1();
            enrichedProduct.setBrand(tuple.getT2());
            enrichedProduct.setCategories(tuple.getT3());
            return enrichedProduct;
        });
    }

    private Mono<BrandModel> getBrandForProduct(ProductModel product) {
        if (product.getBrand() != null && product.getBrand().getId() != null) {
            return brandPersistencePort.findById(product.getBrand().getId());
        }
        return Mono.empty();
    }

    private Mono<List<CategoryModel>> getCategoriesForProduct(Long productId) {
        return productCategoryPersistencePort.findCategoryIdsByProductId(productId)
                .flatMap(categoryPersistencePort::findById)
                .collectList();
    }

    private Mono<ProductModel> updateExistingProduct(ProductModel existingProduct, ProductModel newProductData) {
        existingProduct.setName(newProductData.getName());
        existingProduct.setDescription(newProductData.getDescription());
        existingProduct.setPrice(newProductData.getPrice());
        existingProduct.setStatus(newProductData.getStatus());
        existingProduct.setPhoto(newProductData.getPhoto());
        existingProduct.setBrand(newProductData.getBrand());
        existingProduct.setCategories(newProductData.getCategories());

        return Mono.just(existingProduct);
    }

    private Mono<ProductModel> saveUpdatedProductWithCategories(ProductModel product, Long productId, List<Long> categoryIds) {
        return productPersistencePort.save(product)
                .flatMap(savedProduct ->
                        updateProductCategoryRelations(productId, categoryIds)
                                .then(Mono.just(savedProduct))
                );
    }

    private Mono<Void> updateProductCategoryRelations(Long productId, List<Long> categoryIds) {
        return productCategoryPersistencePort.deleteByProductId(productId)
                .then(productCategoryPersistencePort.saveProductCategories(productId, categoryIds));
    }

    private Mono<Void> checkProductNameUniquenessForUpdate(String productName, Long productId) {
        return productPersistencePort.findByName(productName)
                .flatMap(existingProduct -> validateProductNameForUpdate(existingProduct, productId))
                .then();
    }

    private Mono<Void> validateProductNameForUpdate(ProductModel existingProduct, Long productId) {
        if (!Objects.equals(existingProduct.getId(), productId)) {
            return Mono.error(new DuplicateResourceException(ERROR_PRODUCT_ALREADY_EXISTS));
        }
        return Mono.empty();
    }
}