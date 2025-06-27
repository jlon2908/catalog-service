package com.arka.catalog_service.test;

import com.arka.catalog_service.domain.service.SkuValidationService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SkuValidationServiceTest {
    private final SkuValidationService service = new SkuValidationService();

    @Test
    void testValidSku() {
        assertTrue(service.isValid("ABCD-EFGH-1234"));
        assertTrue(service.isValid("AB-EF-123"));
        assertTrue(service.isValid("ABCD-EFGH-1234-RED"));
    }

    @Test
    void testInvalidSku() {
        assertFalse(service.isValid("A-E-1"));
        assertFalse(service.isValid("1234-ABCD-EFGH"));
        assertFalse(service.isValid(""));
        assertFalse(service.isValid(null));
    }

    @Test
    void testStandardizeValid() {
        String raw = " abcd  efgh 1234 red ";
        String standardized = service.standardize(raw);
        assertEquals("ABCD-EFGH-1234-RED", standardized);
    }

    @Test
    void testStandardizeInvalid() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> service.standardize("A E 1"));
        // Hacemos el test más robusto para evitar fallos por diferencias menores en el mensaje
        assertTrue(ex.getMessage().toLowerCase().contains("brand, type, and model"));
    }
}
