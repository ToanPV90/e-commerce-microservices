package com.mikhailkarpov.auth.service;

import com.mikhailkarpov.auth.dto.CreateUpdateUserRequest;
import com.mikhailkarpov.auth.dto.UserDto;

import java.util.UUID;

public interface UserService {

    UserDto createUser(CreateUpdateUserRequest request);

    UserDto findUserById(UUID id);

    UserDto updateUser(UUID id, CreateUpdateUserRequest update);
}
