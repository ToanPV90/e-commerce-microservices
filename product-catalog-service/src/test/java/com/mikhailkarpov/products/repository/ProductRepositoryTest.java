package com.mikhailkarpov.products.repository;

import com.mikhailkarpov.products.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void initProducts() {
        entityManager.persist(new Product("ABC", "product 1", "description 1", 110, 11));
        entityManager.persist(new Product("DEF", "product 2", "description 2", 120, 12));
        entityManager.persist(new Product("ABC DEF", "prod 3", "description 3", 130, 13));
        entityManager.flush();
    }

    @Test
    void existsByCode() {
        // when
        boolean existsByCodeDEF = productRepository.existsByCode("DEF");
        boolean existsByCodeXYZ = productRepository.existsByCode("XYZ");

        // then
        assertTrue(existsByCodeDEF);
        assertFalse(existsByCodeXYZ);
    }

    @Test
    void findByCode() {
        // when
        Optional<Product> found = productRepository.findByCode("ABC");
        Optional<Product> notFound = productRepository.findByCode("XYZ");

        // then
        assertTrue(found.isPresent());
        assertFalse(notFound.isPresent());
    }

    @Test
    void findAllByNameContaining() {
        // when
        List<Product> products = productRepository.findAllByNameContaining("duct");

        // then
        assertEquals(2, products.size());
    }

    @Test
    void findAllByCodeIn() {
        // when
        List<Product> products = productRepository.findAllByCodeIn(Arrays.asList("ABC", "DEF", "ABC"));

        // then
        assertEquals(2, products.size());
    }
}