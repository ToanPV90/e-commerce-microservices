package com.mikhailkarpov.customers.repository;

import com.mikhailkarpov.customers.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void contextLoads() {
        assertNotNull(dataSource);
        assertNotNull(entityManager);
        assertNotNull(testEntityManager);
        assertNotNull(customerRepository);
    }

    @Test
    void shouldExistsByEmail() {
        // given
        testEntityManager.persistAndFlush(new Customer(UUID.randomUUID(), "fake@example.com"));

        // when
        boolean existsByEmail = customerRepository.existsByEmail("fake@example.com");

        // then
        assertTrue(existsByEmail);
    }

    @Test
    void shouldFindByEmail() {
        // given
        testEntityManager.persistAndFlush(new Customer(UUID.randomUUID(), "fake@example.com"));

        // when
        Optional<Customer> customer = customerRepository.findByEmail("fake@example.com");

        // then
        assertTrue(customer.isPresent());
    }
}