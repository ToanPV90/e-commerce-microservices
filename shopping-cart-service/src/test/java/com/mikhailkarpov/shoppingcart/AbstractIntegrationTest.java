package com.mikhailkarpov.shoppingcart;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;

public class AbstractIntegrationTest {

    public static final GenericContainer redis;
    public static final GenericContainer eureka;

    static {
        redis = new GenericContainer("redis:6-alpine")
                .withExposedPorts(6379)
                .withReuse(true);
        
        eureka = new GenericContainer("springcloud/eureka")
                .withExposedPorts(8761)
                .withReuse(true);

    }

    @DynamicPropertySource
    static void setRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", redis::getContainerIpAddress);
        registry.add("spring.redis.port", redis::getFirstMappedPort);
        registry.add("eureka.client.serviceUrl.defaultZone", AbstractIntegrationTest::getDefaultZone);
    }

    private static String getDefaultZone() {
        return String.format("http://%s:%d/eureka/", eureka.getContainerIpAddress(), eureka.getFirstMappedPort());
    }

    @BeforeAll
    static void startContainers() {
        redis.start();
        eureka.start();
    }
}
