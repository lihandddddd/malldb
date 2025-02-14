package com.custom.mall.service.mapper;

import static com.custom.mall.domain.FlashSaleAsserts.*;
import static com.custom.mall.domain.FlashSaleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FlashSaleMapperTest {

    private FlashSaleMapper flashSaleMapper;

    @BeforeEach
    void setUp() {
        flashSaleMapper = new FlashSaleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFlashSaleSample1();
        var actual = flashSaleMapper.toEntity(flashSaleMapper.toDto(expected));
        assertFlashSaleAllPropertiesEquals(expected, actual);
    }
}
