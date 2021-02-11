package com.mikhailkarpov.customers.repository;

import com.mikhailkarpov.customers.entity.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends CrudRepository<Customer, UUID> {

    boolean existsByEmail(String email);

    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.address WHERE c.email = :email")
    Optional<Customer> findByEmail(@Param("email") String email);
}
