package com.mikhailkarpov.shoppingcart;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;

public class AbstractIntegrationTest {

    static final GenericContainer redis;

    static {
        redis = new GenericContainer("redis:6-alpine")
                .withExposedPorts(6379)
                .withReuse(true);

        redis.start();
    }

    @DynamicPropertySource
    static void setRedisProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.redis.host", redis::getContainerIpAddress);
        dynamicPropertyRegistry.add("spring.redis.port", redis::getFirstMappedPort);
    }
}
