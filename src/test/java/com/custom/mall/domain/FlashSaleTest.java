package com.custom.mall.domain;

import static com.custom.mall.domain.FlashSaleTestSamples.*;
import static com.custom.mall.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.custom.mall.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FlashSaleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FlashSale.class);
        FlashSale flashSale1 = getFlashSaleSample1();
        FlashSale flashSale2 = new FlashSale();
        assertThat(flashSale1).isNotEqualTo(flashSale2);

        flashSale2.setId(flashSale1.getId());
        assertThat(flashSale1).isEqualTo(flashSale2);

        flashSale2 = getFlashSaleSample2();
        assertThat(flashSale1).isNotEqualTo(flashSale2);
    }

    @Test
    void productTest() {
        FlashSale flashSale = getFlashSaleRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        flashSale.addProduct(productBack);
        assertThat(flashSale.getProducts()).containsOnly(productBack);
        assertThat(productBack.getFlashSale()).isEqualTo(flashSale);

        flashSale.removeProduct(productBack);
        assertThat(flashSale.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getFlashSale()).isNull();

        flashSale.products(new HashSet<>(Set.of(productBack)));
        assertThat(flashSale.getProducts()).containsOnly(productBack);
        assertThat(productBack.getFlashSale()).isEqualTo(flashSale);

        flashSale.setProducts(new HashSet<>());
        assertThat(flashSale.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getFlashSale()).isNull();
    }
}
