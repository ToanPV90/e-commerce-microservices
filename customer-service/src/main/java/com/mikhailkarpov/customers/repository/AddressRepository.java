package com.mikhailkarpov.customers.repository;

import com.mikhailkarpov.customers.entity.Address;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends CrudRepository<Address, UUID> {

    Optional<Address> findByCustomerEmail(String email);

}
