package com.arka.micro_catalog.configuration.exception.handler.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionHandlerConstantsTest {

    @Test
    void testPrivateConstructorThrowsException() throws Exception {
        Constructor<ExceptionHandlerConstants> constructor = ExceptionHandlerConstants.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        try {
            constructor.newInstance();
            fail("Expected an IllegalStateException to be thrown");
        } catch (java.lang.reflect.InvocationTargetException e) {
            Throwable cause = e.getCause();
            assertTrue(cause instanceof IllegalStateException);
            assertEquals("Utility class", cause.getMessage());
        }
    }
}
