package com.arka.catalog_service.test;

import com.arka.catalog_service.domain.model.Brand;
import com.arka.catalog_service.infrastructure.adapters.driven.db.Entity.BrandEntity;
import com.arka.catalog_service.infrastructure.adapters.driven.db.Mapper.BrandMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BrandMapperTest {
    @Test
    void toDomain_mapsCorrectly() {
        BrandEntity entity = BrandEntity.builder().id(1L).nombre("Nike").build();
        Brand brand = BrandMapper.toDomain(entity);
        assertEquals(1L, brand.getId());
        assertEquals("Nike", brand.getNombre());
    }

    @Test
    void toEntity_mapsCorrectly() {
        Brand brand = new Brand(2L, "Adidas");
        BrandEntity entity = BrandMapper.toEntity(brand);
        assertEquals(2L, entity.getId());
        assertEquals("Adidas", entity.getNombre());
    }
}
