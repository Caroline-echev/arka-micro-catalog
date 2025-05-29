package com.arka.micro_catalog.adapters.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

class BrandConstantsDrivingTest {

    @Test
    void constructorShouldThrowIllegalStateException() throws Exception {
        Constructor<BrandConstantsDriving> constructor = BrandConstantsDriving.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        try {
            constructor.newInstance();
            fail("Expected IllegalStateException to be thrown");
        } catch (Exception e) {
            Throwable cause = e.getCause();
            assertNotNull(cause);
            assertInstanceOf(IllegalStateException.class, cause);
            assertEquals("Utility class", cause.getMessage());
        }
    }
}
