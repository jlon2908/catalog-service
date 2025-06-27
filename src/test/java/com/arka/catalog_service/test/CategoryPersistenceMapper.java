package com.arka.catalog_service.test;

import com.arka.catalog_service.domain.model.Category;
import com.arka.catalog_service.infrastructure.adapters.driven.db.Entity.CategoryEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryPersistenceMapper {

    @Test
    void toEntity_mapsCorrectly() {
        com.arka.catalog_service.infrastructure.adapters.driven.db.Mapper.CategoryPersistenceMapper mapper = new com.arka.catalog_service.infrastructure.adapters.driven.db.Mapper.CategoryPersistenceMapper();
        Category category = new Category(1L, "Ropa", "Ropa deportiva");
        CategoryEntity entity = mapper.toEntity(category);
        assertEquals(category.getId(), entity.getId());
        assertEquals(category.getName(), entity.getName());
        assertEquals(category.getDescription(), entity.getDescription());
    }

    @Test
    void toModel_mapsCorrectly() {
        com.arka.catalog_service.infrastructure.adapters.driven.db.Mapper.CategoryPersistenceMapper mapper = new com.arka.catalog_service.infrastructure.adapters.driven.db.Mapper.CategoryPersistenceMapper();
        CategoryEntity entity = new CategoryEntity(2L, "Zapatos", "Calzado");
        Category category = mapper.toModel(entity);
        assertEquals(entity.getId(), category.getId());
        assertEquals(entity.getName(), category.getName());
        assertEquals(entity.getDescription(), category.getDescription());
    }
}
