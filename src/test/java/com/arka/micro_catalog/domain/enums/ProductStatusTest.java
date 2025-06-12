package com.arka.micro_catalog.domain.enums;

import com.arka.micro_catalog.domain.enums.ProductStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductStatusTest {

    @Test
    void testIsValid_withValidStatus_shouldReturnTrue() {
        assertTrue(ProductStatus.isValid("ACTIVE"));
        assertTrue(ProductStatus.isValid("inactive"));
        assertTrue(ProductStatus.isValid("Out_Of_Stock"));
        assertTrue(ProductStatus.isValid("to_be_replenished"));
    }

    @Test
    void testIsValid_withInvalidStatus_shouldReturnFalse() {
        assertFalse(ProductStatus.isValid("UNKNOWN"));
        assertFalse(ProductStatus.isValid(""));
        assertFalse(ProductStatus.isValid(null));
    }

    @Test
    void testFromValue_withValidStatus_shouldReturnEnum() {
        assertEquals(ProductStatus.ACTIVE, ProductStatus.fromValue("ACTIVE"));
        assertEquals(ProductStatus.INACTIVE, ProductStatus.fromValue("inactive"));
        assertEquals(ProductStatus.OUT_OF_STOCK, ProductStatus.fromValue("Out_Of_Stock"));
        assertEquals(ProductStatus.TO_BE_REPLENISHED, ProductStatus.fromValue("to_be_replenished"));
    }

    @Test
    void testFromValue_withInvalidStatus_shouldThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ProductStatus.fromValue("INVALID_STATUS");
        });
        assertEquals("Invalid product status: INVALID_STATUS", exception.getMessage());
    }
}
