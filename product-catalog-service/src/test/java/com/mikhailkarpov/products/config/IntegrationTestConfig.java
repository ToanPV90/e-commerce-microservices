package com.mikhailkarpov.products.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@Configuration
public class IntegrationTestConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public PostgreSQLContainer postgreSQLContainer() {
        PostgreSQLContainer container = (PostgreSQLContainer) new PostgreSQLContainer("postgres:12")
                .withDatabaseName("product_catalog_service")
                .withUsername("postgres")
                .withPassword("postgres")
                .withReuse(true);

        return container;
    }

    @Bean
    public DataSource dataSource(PostgreSQLContainer postgreSQLContainer) {
        return DataSourceBuilder.create()
                .driverClassName(postgreSQLContainer().getDriverClassName())
                .url(postgreSQLContainer.getJdbcUrl())
                .username(postgreSQLContainer.getUsername())
                .password(postgreSQLContainer.getPassword())
                .build();
    }

}
