package com.custom.mall;

import com.custom.mall.config.AsyncSyncConfiguration;
import com.custom.mall.config.EmbeddedElasticsearch;
import com.custom.mall.config.EmbeddedKafka;
import com.custom.mall.config.EmbeddedSQL;
import com.custom.mall.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { MallApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedElasticsearch
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
}
