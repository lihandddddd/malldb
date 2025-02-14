package com.custom.mall.service.mapper;

import static com.custom.mall.domain.ShopUserAsserts.*;
import static com.custom.mall.domain.ShopUserTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShopUserMapperTest {

    private ShopUserMapper shopUserMapper;

    @BeforeEach
    void setUp() {
        shopUserMapper = new ShopUserMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getShopUserSample1();
        var actual = shopUserMapper.toEntity(shopUserMapper.toDto(expected));
        assertShopUserAllPropertiesEquals(expected, actual);
    }
}
