package com.custom.mall.service.mapper;

import static com.custom.mall.domain.ProductAsserts.*;
import static com.custom.mall.domain.ProductTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductMapperTest {

    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productMapper = new ProductMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProductSample1();
        var actual = productMapper.toEntity(productMapper.toDto(expected));
        assertProductAllPropertiesEquals(expected, actual);
    }
}
