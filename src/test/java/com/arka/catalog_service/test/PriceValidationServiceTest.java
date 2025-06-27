package com.arka.catalog_service.test;

import com.arka.catalog_service.domain.service.PriceValidationService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PriceValidationServiceTest {
    private final PriceValidationService service = new PriceValidationService();

    @Test
    void testValidPrices() {
        assertTrue(service.isValidPrice("10"));
        assertTrue(service.isValidPrice("10.5"));
        assertTrue(service.isValidPrice("10.55"));
        assertTrue(service.isValidPrice("0.99"));
    }

    @Test
    void testInvalidPrices() {
        assertFalse(service.isValidPrice("10.555"));
        assertFalse(service.isValidPrice("abc"));
        assertFalse(service.isValidPrice("10,50"));
        assertFalse(service.isValidPrice(""));
        assertFalse(service.isValidPrice(null));
    }
}
