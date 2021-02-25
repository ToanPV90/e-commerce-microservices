package com.mikhailkarpov.auth.controller;

import com.mikhailkarpov.auth.AbstractIntegrationTest;
import com.mikhailkarpov.auth.dto.CreateUpdateUserRequest;
import com.mikhailkarpov.auth.dto.UserDto;
import com.mikhailkarpov.auth.entity.AppUser;
import com.mikhailkarpov.auth.repository.AppRoleRepository;
import com.mikhailkarpov.auth.repository.AppUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private AppRoleRepository roleRepository;

    private final JacksonJsonParser jsonParser = new JacksonJsonParser();

    private final CreateUpdateUserRequest request =
            new CreateUpdateUserRequest("test-user", "test-password", Collections.singleton("USER"));

    @Test
    void contextLoads() {
        assertNotNull(restTemplate);
        assertNotNull(userRepository);
        assertNotNull(roleRepository);
    }

    @AfterEach
    void cleanUpUserRepository() {
        Optional<AppUser> user = userRepository.findByUsername("test-user");

        if (user.isPresent()) {
            UUID id = user.get().getId();
            userRepository.deleteById(id);
        }
    }

    @Test
    void givenNoToken_whenPostUsers_thenUnauthorized() {
        //when
        ResponseEntity<UserDto> responseEntity = restTemplate.postForEntity("/users", request, UserDto.class);

        //then
        assertEquals(UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    void givenNoRole_whenPostUsers_thenForbidden() {
        //given
        String accessToken = obtainAccessToken("web-client", "web-client-secret", "demo", "demo-password");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<UserDto> requestEntity = new HttpEntity(request, headers);

        //when
        ResponseEntity<UserDto> responseEntity = restTemplate.exchange("/users", POST, requestEntity, UserDto.class);

        //then
        assertEquals(FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    void givenRole_whenPostUsers_thenCreated() {
        //given
        String accessToken = obtainAccessToken("web-client", "web-client-secret", "admin", "admin-password");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<UserDto> requestEntity = new HttpEntity(request, headers);

        //when
        ResponseEntity<UserDto> responseEntity = restTemplate.exchange("/users", POST, requestEntity, UserDto.class);
        UserDto dto = responseEntity.getBody();

        //then
        assertEquals(CREATED, responseEntity.getStatusCode());
        assertEquals(request.getUsername(), dto.getUsername());
        assertIterableEquals(request.getRoles(), dto.getRoles());

        assertTrue(userRepository.findByUsername("test-user").isPresent());
    }

    @Test
    void givenUsernameIsTaken_whenPostUsers_thenConflict() {
        //given
        userRepository.save(new AppUser("test-user", "test-password"));

        String accessToken = obtainAccessToken("web-client", "web-client-secret", "admin", "admin-password");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<UserDto> requestEntity = new HttpEntity(request, headers);

        //when
        ResponseEntity<UserDto> responseEntity = restTemplate.exchange("/users", POST, requestEntity, UserDto.class);

        //then
        assertEquals(CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    void givenAccessToken_whenGetCurrentUser_thenOk() {
        String accessToken = obtainAccessToken("web-client", "web-client-secret", "demo", "demo-password");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        //when
        ResponseEntity<String> responseEntity = restTemplate.exchange("/users/me", GET, new HttpEntity<>(null, headers), String.class);

        //then
        assertEquals(OK, responseEntity.getStatusCode());
    }

    @Test
    void givenNoAccessToken_whenGetCurrentUser_thenUnauthorized() {
        //when
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/users/me", String.class);

        //then
        assertEquals(UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    void givenAccessToken_whenFindUserById_thenOk() {
        //given
        UUID id = UUID.fromString("748a11b7-6dee-49d8-9485-1441c9ce0e6e");

        String accessToken = obtainAccessToken("web-client", "web-client-secret", "admin", "admin-password");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        //when
        ResponseEntity<UserDto> responseEntity = restTemplate.exchange("/users/{id}", GET, new HttpEntity<>(null, headers), UserDto.class, id);
        UserDto user = responseEntity.getBody();

        //then
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(id, user.getId());
        assertEquals("demo", user.getUsername());
        assertIterableEquals(Collections.singleton("USER"), user.getRoles());
    }

    @Test
    void givenNoAccessToken_whenFindUserById_thenUnauthorized() {
        UUID id = UUID.fromString("748a11b7-6dee-49d8-9485-1441c9ce0e6e");
        ResponseEntity<UserDto> responseEntity = restTemplate.getForEntity("/users/{id}", UserDto.class, id);

        //then
        assertEquals(UNAUTHORIZED, responseEntity.getStatusCode());
    }

    private String obtainAccessToken(String clientId, String clientSecret, String username, String password) {

        ResponseEntity<String> responseEntity = restTemplate
                .withBasicAuth(clientId, clientSecret)
                .postForEntity("/oauth/token?grant_type={grant}&username={user}&password={pass}",
                        null, String.class,
                        "password", username, password);

        assertEquals(OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() != null);

        String body = responseEntity.getBody();

        return jsonParser.parseMap(body).get("access_token").toString();
    }
}