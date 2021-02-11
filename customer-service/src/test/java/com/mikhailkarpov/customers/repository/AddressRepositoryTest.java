package com.mikhailkarpov.customers.repository;

import com.mikhailkarpov.customers.entity.Address;
import com.mikhailkarpov.customers.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AddressRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AddressRepository addressRepository;

    @Test
    void contextLoads() {
        assertNotNull(entityManager);
        assertNotNull(addressRepository);
    }

    @Test
    void shouldFindByCustomerEmail() {
        // given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "fake@example.com");
        customer = entityManager.persistAndFlush(customer);

        Address address = new Address( "zip", "country", "city", "street");
        entityManager.persistAndFlush(address);

        //when
        Optional<Address> optionalAddress = addressRepository.findByCustomerEmail("fake@example.com");

        //then
        assertTrue(optionalAddress.isPresent());
        assertEquals(id, optionalAddress.get().getId());
    }
}