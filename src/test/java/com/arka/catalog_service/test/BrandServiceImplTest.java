package com.arka.catalog_service.test;

import com.arka.catalog_service.application.exception.Brand.DuplicateBrandException;
import com.arka.catalog_service.application.usecase.BrandServiceImpl;
import com.arka.catalog_service.domain.model.Brand;
import com.arka.catalog_service.application.ports.output.BrandPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BrandServiceImplTest {
    private BrandPersistencePort persistencePort;
    private BrandServiceImpl service;

    @BeforeEach
    void setUp() {
        persistencePort = Mockito.mock(BrandPersistencePort.class);
        service = new BrandServiceImpl(persistencePort);
    }

    @Test
    void createBrand_success() {
        Brand brand = new Brand(null, "Nike");
        when(persistencePort.existsByNombre("Nike")).thenReturn(Mono.just(false));
        when(persistencePort.save(any(Brand.class))).thenReturn(Mono.just(new Brand(1L, "Nike")));

        StepVerifier.create(service.create(brand))
                .expectNextMatches(b -> b.getNombre().equals("Nike") && b.getId() != null)
                .verifyComplete();
    }

    @Test
    void createBrand_duplicate() {
        Brand brand = new Brand(null, "Adidas");
        when(persistencePort.existsByNombre("Adidas")).thenReturn(Mono.just(true));

        StepVerifier.create(service.create(brand))
                .expectError(DuplicateBrandException.class)
                .verify();
    }
}

