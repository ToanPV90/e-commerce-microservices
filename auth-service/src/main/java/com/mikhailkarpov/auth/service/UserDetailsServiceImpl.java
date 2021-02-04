package com.mikhailkarpov.auth.service;

import com.mikhailkarpov.auth.entity.AppRole;
import com.mikhailkarpov.auth.entity.AppUser;
import com.mikhailkarpov.auth.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Primary
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AppUserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser user = userRepository.findByUsername(username).orElseThrow(() -> {
            String message = String.format("User with username='%s' not found", username);
            return new UsernameNotFoundException(message);
        });

        UserDetails userDetails = User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().stream().map(AppRole::getName).toArray(String[]::new))
                .build();

        log.debug("Loaded user: {}", userDetails);
        return userDetails;
    }
}
