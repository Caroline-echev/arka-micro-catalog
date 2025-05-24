package com.arka.micro_catalog.adapters.driven.r2dbc.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class ConstantsR2DBCTest {

    @Test
    void constructor_shouldThrowExceptionWhenInstantiated() throws Exception {
        Constructor<ConstantsR2DBC> constructor = ConstantsR2DBC.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);
        Throwable cause = thrown.getCause();

        assertNotNull(cause);
        assertInstanceOf(IllegalStateException.class, cause);
        assertEquals("Utility class", cause.getMessage());
    }
}
