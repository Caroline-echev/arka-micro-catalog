package com.arka.micro_catalog.domain.util.constants;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

class CategoryConstantsTest {

    @Test
    void constructorShouldThrowIllegalStateException() throws Exception {
        Constructor<CategoryConstants> constructor = CategoryConstants.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        Exception exception = assertThrows(Exception.class, constructor::newInstance);
        assertNotNull(exception.getCause());
        assertInstanceOf(IllegalStateException.class, exception.getCause());
        assertEquals("Utility class", exception.getCause().getMessage());
    }

    @Test
    void constantsShouldHaveExpectedValues() {
        assertEquals("Category already exists", CategoryConstants.CATEGORY_ALREADY_EXISTS);
        assertEquals("Category not found", CategoryConstants.CATEGORY_NOT_FOUND);
    }
}
