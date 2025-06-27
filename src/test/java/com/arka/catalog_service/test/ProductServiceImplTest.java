package com.arka.catalog_service.test;

import com.arka.catalog_service.application.exception.product.DuplicateSkuException;
import com.arka.catalog_service.application.ports.output.ProductPersistencePort;
import com.arka.catalog_service.application.usecase.ProductServiceImpl;
import com.arka.catalog_service.domain.model.Brand;
import com.arka.catalog_service.domain.model.Product;
import com.arka.catalog_service.domain.service.PriceValidationService;
import com.arka.catalog_service.domain.service.SkuValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceImplTest {
    private ProductPersistencePort persistencePort;
    private PriceValidationService priceValidationService;
    private SkuValidationService skuValidationService;
    private ProductServiceImpl service;

    @BeforeEach
    void setUp() {
        persistencePort = Mockito.mock(ProductPersistencePort.class);
        priceValidationService = new PriceValidationService();
        skuValidationService = new SkuValidationService();
        service = new ProductServiceImpl(persistencePort, priceValidationService, skuValidationService);
    }

    @Test
    void createProduct_success() {
        Product product = Product.builder()
                .id(null)
                .name("Camiseta")
                .description("Camiseta deportiva")
                .unitPrice(new BigDecimal("19.99"))
                .sku("NIKE SPORT 1234")
                .active(true)
                .brand(new Brand(1L, "NIKE"))
                .categories(Collections.emptyList())
                .features(Collections.emptyList())
                .build();

        when(persistencePort.findBySku("NIKE-SPORT-1234")).thenReturn(Flux.empty());
        when(persistencePort.save(any(Product.class))).thenReturn(Mono.just(product.toBuilder().id(1L).sku("NIKE-SPORT-1234").build()));

        StepVerifier.create(service.createProduct(product))
                .expectNextMatches(p -> p.getId() != null && p.getSku().equals("NIKE-SPORT-1234"))
                .verifyComplete();
    }

    @Test
    void createProduct_duplicateSku() {
        Product product = Product.builder()
                .id(null)
                .name("Camiseta")
                .description("Camiseta deportiva")
                .unitPrice(new BigDecimal("19.99"))
                .sku("NIKE SPORT 1234")
                .active(true)
                .brand(new Brand(1L, "NIKE"))
                .categories(Collections.emptyList())
                .features(Collections.emptyList())
                .build();

        when(persistencePort.findBySku("NIKE-SPORT-1234")).thenReturn(Flux.just(product));

        StepVerifier.create(service.createProduct(product))
                .expectError(DuplicateSkuException.class)
                .verify();
    }
}

