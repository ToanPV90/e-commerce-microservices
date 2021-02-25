package com.mikhailkarpov.auth;

import org.junit.jupiter.api.AfterAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public class AbstractIntegrationTest {

    static PostgreSQLContainer postgres;

    static {
        postgres = new PostgreSQLContainer("postgres:12-alpine")
                .withDatabaseName("auth_service")
                .withUsername("auth_service")
                .withPassword("auth_service");

        postgres.start();
    }

    @DynamicPropertySource
    static void configDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @DynamicPropertySource
    static void configEureka(DynamicPropertyRegistry registry) {
        registry.add("eureka.client.enabled", AbstractIntegrationTest::disableEureka);
    }

    private static boolean disableEureka() {
        return false;
    }

    @AfterAll
    static void stopContainers() {
        postgres.stop();
    }
}
