package com.custom.mall.domain;

import static com.custom.mall.domain.OrderTestSamples.*;
import static com.custom.mall.domain.ShopUserTestSamples.*;
import static com.custom.mall.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.custom.mall.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ShopUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShopUser.class);
        ShopUser shopUser1 = getShopUserSample1();
        ShopUser shopUser2 = new ShopUser();
        assertThat(shopUser1).isNotEqualTo(shopUser2);

        shopUser2.setId(shopUser1.getId());
        assertThat(shopUser1).isEqualTo(shopUser2);

        shopUser2 = getShopUserSample2();
        assertThat(shopUser1).isNotEqualTo(shopUser2);
    }

    @Test
    void orderTest() {
        ShopUser shopUser = getShopUserRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        shopUser.addOrder(orderBack);
        assertThat(shopUser.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getShopUser()).isEqualTo(shopUser);

        shopUser.removeOrder(orderBack);
        assertThat(shopUser.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getShopUser()).isNull();

        shopUser.orders(new HashSet<>(Set.of(orderBack)));
        assertThat(shopUser.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getShopUser()).isEqualTo(shopUser);

        shopUser.setOrders(new HashSet<>());
        assertThat(shopUser.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getShopUser()).isNull();
    }

    @Test
    void userProfileTest() {
        ShopUser shopUser = getShopUserRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        shopUser.setUserProfile(userProfileBack);
        assertThat(shopUser.getUserProfile()).isEqualTo(userProfileBack);
        assertThat(userProfileBack.getShopUser()).isEqualTo(shopUser);

        shopUser.userProfile(null);
        assertThat(shopUser.getUserProfile()).isNull();
        assertThat(userProfileBack.getShopUser()).isNull();
    }
}
