package com.mikhailkarpov.auth.controller;

import com.mikhailkarpov.auth.dto.CreateUpdateUserRequest;
import com.mikhailkarpov.auth.dto.UserDto;
import com.mikhailkarpov.auth.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.UUID;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final UserDto testUser = new UserDto(UUID.randomUUID(), "test-user", Collections.singleton("test-role"));

    @Test
    void givenValidRequest_whenPostUsers_thenOk() throws Exception {
        //given
        CreateUpdateUserRequest request = new CreateUpdateUserRequest("test-user", "test-password", Collections.singleton("test-role"));
        when(userService.createUser(request)).thenReturn(testUser);

        mockMvc.perform(post("/users")
                .contentType("application/json;charset=UTF-8")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/users/" + testUser.getId()))
                .andExpect(jsonPath("$.id").value(testUser.getId().toString()))
                .andExpect(jsonPath("$.username").value("test-user"))
                .andExpect(jsonPath("$.roles").isArray())
                .andExpect(jsonPath("$.roles", hasSize(1)))
                .andExpect(jsonPath("$.roles[0]").value("test-role"));

        verify(userService).createUser(request);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void givenUser_whenGetUser_thenOk() throws Exception {
        //given
        when(userService.findUserById(testUser.getId())).thenReturn(testUser);

        //when
        mockMvc.perform(get("/users/{id}", testUser.getId())
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testUser.getId().toString()))
                .andExpect(jsonPath("$.username").value("test-user"))
                .andExpect(jsonPath("$.roles").isArray())
                .andExpect(jsonPath("$.roles", hasSize(1)));

        verify(userService).findUserById(testUser.getId());
        verifyNoMoreInteractions(userService);
    }

    @ParameterizedTest
    @MethodSource("getInvalidUsers")
    void givenInValidRequest_whenPostUsers_thenBadRequest(CreateUpdateUserRequest request) throws Exception {

        mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoMoreInteractions(userService);
    }

    private static Stream<Arguments> getInvalidUsers() {
        return Stream.of(
                Arguments.of(new CreateUpdateUserRequest(null, "test-password", Collections.singleton("test-role"))),
                Arguments.of(new CreateUpdateUserRequest("test-user", null, Collections.singleton("test-role"))),
                Arguments.of(new CreateUpdateUserRequest("test-user", "test-password", null)),
                Arguments.of(new CreateUpdateUserRequest("", "test-password", null)),
                Arguments.of(new CreateUpdateUserRequest("test-user", "", Collections.singleton("test-role"))),
                Arguments.of(new CreateUpdateUserRequest("test-user", "test-password", Collections.emptySet()))
        );
    }

    @Test
    void whenGetUsersMe_thenOk() throws Exception {
        mockMvc.perform(get("/users/me").accept(APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
