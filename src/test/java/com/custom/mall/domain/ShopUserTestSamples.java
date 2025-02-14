package com.custom.mall.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ShopUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ShopUser getShopUserSample1() {
        return new ShopUser().id(1L).username("username1").email("email1");
    }

    public static ShopUser getShopUserSample2() {
        return new ShopUser().id(2L).username("username2").email("email2");
    }

    public static ShopUser getShopUserRandomSampleGenerator() {
        return new ShopUser().id(longCount.incrementAndGet()).username(UUID.randomUUID().toString()).email(UUID.randomUUID().toString());
    }
}
