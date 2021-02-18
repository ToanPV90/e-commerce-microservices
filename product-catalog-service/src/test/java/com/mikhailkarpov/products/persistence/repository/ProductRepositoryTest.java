package com.mikhailkarpov.products.persistence.repository;

import com.mikhailkarpov.products.persistence.entity.Product;
import com.mikhailkarpov.products.persistence.specification.ProductSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.domain.Specification;
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
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:12")
            .withDatabaseName("product_catalog_service")
            .withUsername("postgres")
            .withPassword("postgres");

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    private final ProductSpecification productSpecification = new ProductSpecification();

    @DynamicPropertySource
    static void setPostgreSQLContainer(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    }

    @Test
    void contextLoads() {
        assertNotNull(entityManager);
        assertNotNull(productRepository);
        assertNotNull(productSpecification);
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
    void findAll() {
        //given flyway migration test

        //when
        List<Product> all = productRepository.findAll();

        //then
        assertEquals(8, all.size());
    }

    @Test
    void findAllByCategoriesId() {
        //given flyway migration test
        Specification<Product> specification = productSpecification.categoryIdEquals(3);

        //when
        List<Product> byCategoriesId = productRepository.findAllByCategoriesId(3);
        List<Product> byCategoriesSpecification = productRepository.findAll(specification);

        //then
        assertEquals(2, byCategoriesId.size());
        assertEquals(2, byCategoriesSpecification.size());
    }

    @Test
    void findAllByCodeIn() {
        //given flyway migration test
        Specification<Product> specification = productSpecification.codeIn(Arrays.asList("dell-27", "dell-21"));

        //when
        List<Product> byCodeIn = productRepository.findAllByCodeIn(Arrays.asList("dell-27", "dell-21"));
        List<Product> byCodeSpecification = productRepository.findAll(specification);

        //then
        assertEquals(2, byCodeIn.size());
        assertEquals(2, byCodeSpecification.size());
    }

    @Test
    void findAllByNameLike() {
        //given flyway migration test
        Specification<Product> specification = productSpecification.nameLike("%DeLL%");

        //when
        List<Product> byNameLike = productRepository.findAllByNameContainingIgnoreCase("dell");
        List<Product> byNameSpecification = productRepository.findAll(specification);

        //then
        assertEquals(2, byNameLike.size());
        assertEquals(2, byNameSpecification.size());
    }

    @Test
    void findByCode() {
        //given flyway migration test

        //when
        Optional<Product> product = productRepository.findByCode("samsung");

        //then
        assertTrue(product.isPresent());
    }
}