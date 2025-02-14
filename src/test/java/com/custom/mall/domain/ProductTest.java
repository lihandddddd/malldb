package com.custom.mall.domain;

import static com.custom.mall.domain.FlashSaleTestSamples.*;
import static com.custom.mall.domain.OrderTestSamples.*;
import static com.custom.mall.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.custom.mall.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void flashSaleTest() {
        Product product = getProductRandomSampleGenerator();
        FlashSale flashSaleBack = getFlashSaleRandomSampleGenerator();

        product.setFlashSale(flashSaleBack);
        assertThat(product.getFlashSale()).isEqualTo(flashSaleBack);

        product.flashSale(null);
        assertThat(product.getFlashSale()).isNull();
    }

    @Test
    void orderTest() {
        Product product = getProductRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        product.addOrder(orderBack);
        assertThat(product.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getProduct()).isEqualTo(product);

        product.removeOrder(orderBack);
        assertThat(product.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getProduct()).isNull();

        product.orders(new HashSet<>(Set.of(orderBack)));
        assertThat(product.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getProduct()).isEqualTo(product);

        product.setOrders(new HashSet<>());
        assertThat(product.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getProduct()).isNull();
    }
}
