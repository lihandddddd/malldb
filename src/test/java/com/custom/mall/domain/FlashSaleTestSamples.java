package com.custom.mall.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FlashSaleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static FlashSale getFlashSaleSample1() {
        return new FlashSale().id(1L).name("name1").maxQuantity(1);
    }

    public static FlashSale getFlashSaleSample2() {
        return new FlashSale().id(2L).name("name2").maxQuantity(2);
    }

    public static FlashSale getFlashSaleRandomSampleGenerator() {
        return new FlashSale().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).maxQuantity(intCount.incrementAndGet());
    }
}
