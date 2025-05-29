package com.arka.micro_catalog.domain.util.constants;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

class ProductConstantsTest {

    @Test
    void constructorShouldThrowIllegalStateException() throws Exception {
        Constructor<ProductConstants> constructor = ProductConstants.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        Exception exception = assertThrows(Exception.class, constructor::newInstance);
        assertNotNull(exception.getCause());
        assertInstanceOf(IllegalStateException.class, exception.getCause());
        assertEquals("Utility class", exception.getCause().getMessage());
    }

    @Test
    void constantsShouldHaveExpectedValues() {
        assertEquals(3, ProductConstants.MAX_CATEGORY_COUNT);
        assertEquals(1, ProductConstants.MIN_CATEGORY_COUNT);

        assertEquals("Brand not found", ProductConstants.ERROR_BRAND_NOT_FOUND);
        assertEquals("Product must have 1 to 3 categories", ProductConstants.ERROR_CATEGORY_COUNT_INVALID);
        assertEquals("Product categories must not be repeated", ProductConstants.ERROR_CATEGORY_DUPLICATE);
        assertEquals("Some categories not found", ProductConstants.ERROR_CATEGORIES_NOT_FOUND);
        assertEquals("Product already exists", ProductConstants.ERROR_PRODUCT_ALREADY_EXISTS);
    }
}
