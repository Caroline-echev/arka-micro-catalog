package com.arka.micro_catalog.configuration.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class ConstantsConfigurationTest {

    @Test
    void constructor_shouldThrowExceptionWhenInstantiated() throws Exception {
        Constructor<ConstantsConfiguration> constructor = ConstantsConfiguration.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);
        Throwable cause = thrown.getCause();

        assertNotNull(cause);
        assertInstanceOf(IllegalStateException.class, cause);
        assertEquals("Utility class", cause.getMessage());
    }


    @Test
    void constants_shouldHaveExpectedValues() {
        assertEquals("ERR_VALIDATION", ConstantsConfiguration.VALIDATION_ERROR_CODE);
        assertEquals("An unexpected error occurred: ", ConstantsConfiguration.INTERNAL_ERROR_PREFIX);
        assertEquals("Error processing response", ConstantsConfiguration.ERROR_PROCESSING_RESPONSE);
        assertEquals("Error writing error response as JSON", ConstantsConfiguration.ERROR_WRITING_JSON);
        assertEquals("migrate", ConstantsConfiguration.MIGRATE);
        assertEquals("Micro Catalog API", ConstantsConfiguration.API_TITLE);
        assertEquals("API for catalog management", ConstantsConfiguration.API_DESCRIPTION);
        assertEquals("v1.0", ConstantsConfiguration.API_VERSION);
        assertEquals("catalog", ConstantsConfiguration.API_GROUP);
        assertEquals("/api/**", ConstantsConfiguration.API_PATHS);
    }
}
