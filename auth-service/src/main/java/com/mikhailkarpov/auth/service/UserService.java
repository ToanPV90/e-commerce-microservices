package com.mikhailkarpov.auth.service;

import com.mikhailkarpov.auth.dto.UserDto;

import java.util.UUID;

public interface UserService {

    UserDto createUser(UserDto user);

    UserDto findUserById(UUID id);

}
