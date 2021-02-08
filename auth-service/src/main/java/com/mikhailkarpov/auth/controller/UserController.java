package com.mikhailkarpov.auth.controller;

import com.mikhailkarpov.auth.dto.UserDto;
import com.mikhailkarpov.auth.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.security.Principal;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto user,
                                              UriComponentsBuilder uriComponentsBuilder) {
        log.info("Request to create user: {}", user);
        UserDto created = userService.createUser(user);

        return ResponseEntity
                .created(uriComponentsBuilder.path("/users/{id}").build(created.getId()))
                .body(created);
    }

    @GetMapping("/me")
    public Principal getCurrentUser(@AuthenticationPrincipal Principal principal) {
        log.info("Request for a current user: ", principal);
        return principal;
    }

    @GetMapping("/{id}")
    public UserDto findUserById(@PathVariable UUID id) {
        log.info("Request for a user with id={}", id);
        return userService.findUserById(id);
    }
}
