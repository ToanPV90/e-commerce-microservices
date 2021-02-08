package com.mikhailkarpov.customers.service;

import com.mikhailkarpov.customers.dto.CustomerDto;
import com.mikhailkarpov.customers.entity.Customer;
import com.mikhailkarpov.customers.exception.ResourceNotFoundException;
import com.mikhailkarpov.customers.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional(readOnly = true)
    public CustomerDto findCustomerById(UUID id) {

        Optional<Customer> found = customerRepository.findById(id);

        if (!found.isPresent()) {
            String message = String.format("Customer with id = %s not found", id);
            log.warn(message);
            throw new ResourceNotFoundException(message);
        }

        Customer customer = found.get();
        log.info("Found customer: {}", customer);
        return mapFromEntity(customer);
    }

    private CustomerDto mapFromEntity(Customer customer) {
        return new CustomerDto(customer.getId(), customer.getEmail());
    }
}
