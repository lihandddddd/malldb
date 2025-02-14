package com.custom.mall.domain;

import static com.custom.mall.domain.OrderTestSamples.*;
import static com.custom.mall.domain.ProductTestSamples.*;
import static com.custom.mall.domain.ShopUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.custom.mall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Order.class);
        Order order1 = getOrderSample1();
        Order order2 = new Order();
        assertThat(order1).isNotEqualTo(order2);

        order2.setId(order1.getId());
        assertThat(order1).isEqualTo(order2);

        order2 = getOrderSample2();
        assertThat(order1).isNotEqualTo(order2);
    }

    @Test
    void productTest() {
        Order order = getOrderRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        order.setProduct(productBack);
        assertThat(order.getProduct()).isEqualTo(productBack);

        order.product(null);
        assertThat(order.getProduct()).isNull();
    }

    @Test
    void shopUserTest() {
        Order order = getOrderRandomSampleGenerator();
        ShopUser shopUserBack = getShopUserRandomSampleGenerator();

        order.setShopUser(shopUserBack);
        assertThat(order.getShopUser()).isEqualTo(shopUserBack);

        order.shopUser(null);
        assertThat(order.getShopUser()).isNull();
    }
}
