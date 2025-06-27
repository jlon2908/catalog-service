package com.arka.catalog_service.test;

import com.arka.catalog_service.application.exception.Category.DuplicateCategoryException;
import com.arka.catalog_service.application.usecase.CategoryServiceImpl;
import com.arka.catalog_service.domain.model.Category;
import com.arka.catalog_service.application.ports.output.CategoryPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CategoryServiceImplTest {
    private CategoryPersistencePort persistencePort;
    private CategoryServiceImpl service;

    @BeforeEach
    void setUp() {
        persistencePort = Mockito.mock(CategoryPersistencePort.class);
        service = new CategoryServiceImpl(persistencePort);
    }

    @Test
    void createCategory_success() {
        Category category = new Category(null, "Ropa", "Ropa deportiva");
        when(persistencePort.existsByName("Ropa")).thenReturn(Mono.just(false));
        when(persistencePort.save(any(Category.class))).thenReturn(Mono.just(new Category(1L, "Ropa", "Ropa deportiva")));

        StepVerifier.create(service.create(category))
                .expectNextMatches(c -> c.getName().equals("Ropa") && c.getId() != null)
                .verifyComplete();
    }

    @Test
    void createCategory_duplicate() {
        Category category = new Category(null, "Zapatos", "Calzado");
        when(persistencePort.existsByName("Zapatos")).thenReturn(Mono.just(true));

        StepVerifier.create(service.create(category))
                .expectError(DuplicateCategoryException.class)
                .verify();
    }
}

