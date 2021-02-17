package com.mikhailkarpov.products.repository;

import com.mikhailkarpov.products.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    @Container
    static PostgreSQLContainer postgreSQLContainer =
            (PostgreSQLContainer) new PostgreSQLContainer("postgres:12")
                    .withDatabaseName("product_catalog_service")
                    .withUsername("postgres")
                    .withPassword("postgres")
                    .withReuse(true);

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @DynamicPropertySource
    static void setPostgreSQLContainer(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    }

    @Test
    void contextLoads() {
        assertNotNull(entityManager);
        assertNotNull(productRepository);
    }

    @Test
    void existsByCode() {
        //given flyway migration test

        //when
        boolean existsByCode = productRepository.existsByCode("apple");

        //then
        assertTrue(existsByCode);
    }

    @Test
    void findAllByCategoriesId() {
        //given flyway migration test

        //when
        List<Product> found = productRepository.findAllByCategoriesId(3);

        //then
        assertEquals(2, found.size());
    }

    @Test
    void findAllByCodeIn() {
        //given flyway migration test

        //when
        List<Product> found = productRepository.findAllByCodeIn(Arrays.asList("dell-27", "dell-21"));

        //then
        assertEquals(2, found.size());
    }

    @Test
    void findAllByNameLike() {
        //given flyway migration test

        //when
        List<Product> found = productRepository.findAllByNameContainingIgnoreCase("DELL");

        //then
        assertEquals(2, found.size());
    }

    @Test
    void findByCode() {
        //given flyway migration test

        //when
        Optional<Product> product = productRepository.findByCode("samsung");

        //then
        assertTrue(product.isPresent());
    }

    @Test
    void search() {
        //given flyway migration test

        //when
        List<Product> dell21 = productRepository.search("DELL 21", Arrays.asList("dell-27", "dell-21"));
        List<Product> dell21_and27 = productRepository.search("", Arrays.asList("dell-27", "dell-21"));

        //then
        assertEquals(1, dell21.size());
        assertEquals(2, dell21_and27.size());
    }
}