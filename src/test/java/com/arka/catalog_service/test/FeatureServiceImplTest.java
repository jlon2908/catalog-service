package com.arka.catalog_service.test;

import com.arka.catalog_service.application.exception.Feature.DuplicateFeatureException;
import com.arka.catalog_service.application.usecase.FeatureServiceImpl;
import com.arka.catalog_service.domain.model.Feature;
import com.arka.catalog_service.application.ports.output.FeaturePersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FeatureServiceImplTest {
    private FeaturePersistencePort persistencePort;
    private FeatureServiceImpl service;

    @BeforeEach
    void setUp() {
        persistencePort = Mockito.mock(FeaturePersistencePort.class);
        service = new FeatureServiceImpl(persistencePort);
    }

    @Test
    void createFeature_success() {
        Feature feature = new Feature(null, "Color");
        when(persistencePort.findByName("Color")).thenReturn(Mono.empty());
        when(persistencePort.save(any(Feature.class))).thenReturn(Mono.just(new Feature(1L, "Color")));

        StepVerifier.create(service.create(feature))
                .expectNextMatches(f -> f.getName().equals("Color") && f.getId() != null)
                .verifyComplete();
    }

    @Test
    void createFeature_duplicate() {
        Feature feature = new Feature(null, "Talla");
        when(persistencePort.findByName("Talla")).thenReturn(Mono.just(new Feature(2L, "Talla")));
        when(persistencePort.save(any(Feature.class))).thenReturn(Mono.just(new Feature(2L, "Talla")));

        StepVerifier.create(service.create(feature))
                .expectError(DuplicateFeatureException.class)
                .verify();
    }
}
