package com.arka.micro_catalog.adapters.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

class CategoryConstantsDrivingTest {

    @Test
    void constructorShouldThrowIllegalStateException() throws Exception {
        Constructor<CategoryConstantsDriving> constructor = CategoryConstantsDriving.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        Exception exception = assertThrows(Exception.class, constructor::newInstance);
        assertNotNull(exception.getCause());
        assertInstanceOf(IllegalStateException.class, exception.getCause());
        assertEquals("Utility class", exception.getCause().getMessage());
    }
}
