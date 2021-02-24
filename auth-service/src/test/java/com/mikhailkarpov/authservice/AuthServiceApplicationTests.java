package com.mikhailkarpov.authservice;

import com.mikhailkarpov.auth.AuthServiceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AuthServiceApplication.class)
class AuthServiceApplicationTests extends AbstractIntegrationTest {

    @Test
    void contextLoads() {
    }
}
