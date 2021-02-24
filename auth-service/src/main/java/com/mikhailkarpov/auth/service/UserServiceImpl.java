package com.mikhailkarpov.auth.service;

import com.mikhailkarpov.auth.dto.UserDto;
import com.mikhailkarpov.auth.entity.AppRole;
import com.mikhailkarpov.auth.entity.AppUser;
import com.mikhailkarpov.auth.exception.ResourceAlreadyExistsException;
import com.mikhailkarpov.auth.exception.ResourceNotFoundException;
import com.mikhailkarpov.auth.repository.AppRoleRepository;
import com.mikhailkarpov.auth.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final AppUserRepository userRepository;
    private final AppRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto createUser(UserDto user) {
        String username = user.getUsername();
        String password = passwordEncoder.encode(user.getPassword());

        throwIfExistsByUsername(username);

        Set<AppRole> roles = getOrCreateRoles(user.getRoles());

        AppUser created = userRepository.save(new AppUser(username, password, roles));
        log.info("Creating user: {}", created);

        return mapFromEntity(created);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findUserById(UUID id) {
        Optional<AppUser> found = userRepository.findById(id);

        if (!found.isPresent()) {
            String message = String.format("User with id=%s not found", id);
            throw new ResourceNotFoundException(message);
        }

        AppUser user = found.get();
        log.info("Found user: {}", user);

        return mapFromEntity(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(UUID id, UserDto update) {

        AppUser user = getUserOrElseThrow(id);

        String username = update.getUsername();
        if (!username.equals(user.getUsername())) {
            throwIfExistsByUsername(username);
            user.setUsername(username);
        }

        String encodedPassword = passwordEncoder.encode(update.getPassword());
        user.setPassword(encodedPassword);

        Set<AppRole> roles = getOrCreateRoles(update.getRoles());
        user.setRoles(roles);

        return mapFromEntity(user);
    }

    /** HELPERS */

    private void throwIfExistsByUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            String message = String.format("User with username='%s' already exists", username);
            log.warn(message);
            throw new ResourceAlreadyExistsException(message);
        }
    }

    private Set<AppRole> getOrCreateRoles(Set<String> rolesNames) {
        return rolesNames.stream()
                .map(roleName -> roleRepository.findByName(roleName).orElse(new AppRole(roleName)))
                .collect(Collectors.toSet());
    }

    private AppUser getUserOrElseThrow(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> {
            String message = String.format("User with id=%s not found", id);
            return new ResourceNotFoundException(message);
        });
    }

    private UserDto mapFromEntity(AppUser user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().stream().map(AppRole::getName).collect(Collectors.toSet()))
                .build();
    }
}
