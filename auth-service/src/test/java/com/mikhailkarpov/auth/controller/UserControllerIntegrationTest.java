package com.mikhailkarpov.auth.controller;

import com.mikhailkarpov.auth.dto.UserDto;
import com.mikhailkarpov.auth.repository.AppRoleRepository;
import com.mikhailkarpov.auth.repository.AppUserRepository;
import com.mikhailkarpov.authservice.AbstractIntegrationTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private AppRoleRepository roleRepository;

    @Test
    void contextLoads() {
        assertNotNull(restTemplate);
        assertNotNull(userRepository);
        assertNotNull(roleRepository);
    }

    @Disabled
    @Test
    void createUser() {
    }

    @Disabled
    @Test
    void getCurrentUser() {
    }

    @Disabled
    @Test
    void findUserById() {
    }

    @Disabled
    @Test
    void updateUser() {
    }
}