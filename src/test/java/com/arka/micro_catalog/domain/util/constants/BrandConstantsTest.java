package com.arka.micro_catalog.domain.util.constants;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

class BrandConstantsTest {

    @Test
    void constructorShouldThrowIllegalStateException() throws Exception {
        Constructor<BrandConstants> constructor = BrandConstants.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        Exception exception = assertThrows(Exception.class, constructor::newInstance);
        assertNotNull(exception.getCause());
        assertInstanceOf(IllegalStateException.class, exception.getCause());
        assertEquals("Utility class", exception.getCause().getMessage());
    }

    @Test
    void constantsShouldHaveExpectedValues() {
        assertEquals("Brand already exists", BrandConstants.BRAND_ALREADY_EXISTS);
        assertEquals("Brand not found", BrandConstants.BRAND_NOT_FOUND);
    }
}
