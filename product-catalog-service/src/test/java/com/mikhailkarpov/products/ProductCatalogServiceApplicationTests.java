package com.mikhailkarpov.products;

import com.mikhailkarpov.products.config.IntegrationTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(IntegrationTestConfig.class)
@SpringBootTest
class ProductCatalogServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
