package com.mikhailkarpov.auth.controller;

import com.mikhailkarpov.auth.dto.UserDto;
import com.mikhailkarpov.auth.exception.BadRequestException;
import com.mikhailkarpov.auth.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto user,
                                              UriComponentsBuilder uriComponentsBuilder) {
        log.info("Request to create user: {}", user);
        UserDto created = userService.createUser(user);

        return ResponseEntity
                .created(uriComponentsBuilder.path("/users/{id}").build(created.getId()))
                .body(created);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public Principal getCurrentUser(@AuthenticationPrincipal Principal principal) {
        log.info("Request for a current user: ", principal);
        return principal;
    }

    @GetMapping("/{id}")
    public UserDto findUserById(@PathVariable UUID id) {
        log.info("Request for a user with id={}", id);
        return userService.findUserById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public UserDto updateUser(@PathVariable UUID id, @Valid @RequestBody UserDto update) {
        log.info("Request to update user with id={}: {}", id, update);

        if (!id.equals(update.getId())) {
            String message = String.format("URI /users/{} don't match user id={}", id, update.getId());
            throw new BadRequestException(message);
        }

        return userService.updateUser(id, update);
    }
}
