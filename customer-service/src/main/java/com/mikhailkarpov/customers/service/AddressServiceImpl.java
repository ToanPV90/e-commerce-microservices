package com.mikhailkarpov.customers.service;

import com.mikhailkarpov.customers.dto.AddressDto;
import com.mikhailkarpov.customers.entity.Address;
import com.mikhailkarpov.customers.entity.Customer;
import com.mikhailkarpov.customers.exception.ConflictException;
import com.mikhailkarpov.customers.exception.ResourceNotFoundException;
import com.mikhailkarpov.customers.repository.AddressRepository;
import com.mikhailkarpov.customers.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public AddressDto createAddress(String customerName, AddressDto address) {

        Customer customer = customerRepository.findByEmail(customerName).orElseThrow(() -> {
            String message = String.format("Customer with email '%s' not found", customerName);
            log.error(message);
            return new ResourceNotFoundException(message);
        });

        if (customer.getAddress() != null) {
            String message = String.format("Address for customer with email '%s' already exists", customerName);
            log.warn(message);
            throw new ConflictException(message);
        }

        Address created = new Address(address.getZip(), address.getCountry(), address.getCity(), address.getStreet());
        created.setCustomer(customer);
        created = addressRepository.save(created);
        log.info("Creating {}", address);

        address.setId(created.getId());
        return address;
    }

    @Override
    @Transactional(readOnly = true)
    public AddressDto findAddressByCustomerName(String customerName) {

        Optional<Address> optionalAddress = addressRepository.findByCustomerEmail(customerName);

        if (!optionalAddress.isPresent()) {
            String message = String.format("Address for customer with email '%s' not found", customerName);
            log.warn(message);
            throw new ResourceNotFoundException(message);
        }

        Address address = optionalAddress.get();
        log.info("Found {}", address);

        return AddressDto.builder()
                .id(address.getId())
                .zip(address.getZip())
                .country(address.getCountry())
                .city(address.getCity())
                .street(address.getStreet())
                .build();
    }

    @Override
    @Transactional
    public AddressDto updateAddress(String customerName, AddressDto update) {
        Optional<Address> optionalAddress = addressRepository.findByCustomerEmail(customerName);

        if (!optionalAddress.isPresent()) {
            String message = String.format("Address for customer with email '%s' not found", customerName);
            log.warn(message);
            throw new ResourceNotFoundException(message);
        }

        Address address = optionalAddress.get();
        address.setZip(update.getZip());
        address.setCountry(update.getCountry());
        address.setCity(update.getCity());
        address.setStreet(update.getStreet());
        log.info("Updating {}", address);

        update.setId(address.getId());
        return update;
    }
}
