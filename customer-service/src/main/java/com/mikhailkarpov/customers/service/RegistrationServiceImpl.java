package com.mikhailkarpov.customers.service;

import com.mikhailkarpov.customers.dto.UserDto;
import com.mikhailkarpov.customers.entity.Customer;
import com.mikhailkarpov.customers.exception.ConflictException;
import com.mikhailkarpov.customers.repository.CustomerRepository;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Slf4j
@Service
@AllArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final CustomerRepository customerRepository;
    private final AuthService authService;

    @Override
    @Transactional
    public void registerCustomer(String email, String password) {

        if (customerRepository.existsByEmail(email)) {
            String message = String.format("Customer with email='%s' already exists", email);
            log.warn(message);
            throw new ConflictException(message);
        }

        UserDto user = authService.createUser(UserDto.builder()
                .username(email)
                .password(password)
                .roles(Collections.singleton("CUSTOMER"))
                .build());

        Customer customer = customerRepository.save(new Customer(user.getId(), email));
        log.info("Creating customer: {}", customer);
    }

}
