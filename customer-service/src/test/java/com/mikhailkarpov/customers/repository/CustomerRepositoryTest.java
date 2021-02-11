package com.mikhailkarpov.customers.repository;

import com.mikhailkarpov.customers.entity.Address;
import com.mikhailkarpov.customers.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void shouldCascadePersistAddress() {
        // given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "fake@example.com");
        customer.setAddress(new Address("zip", "country", "city", "street"));
        testEntityManager.persistAndFlush(customer);

        //when
        List<Address> addresses = entityManager
                .createQuery("SELECT a FROM Address a WHERE a.zip = :zip", Address.class)
                .setParameter("zip", "zip")
                .getResultList();

        //then
        assertEquals(1, addresses.size());
        assertEquals(id, addresses.get(0).getId());
    }
}