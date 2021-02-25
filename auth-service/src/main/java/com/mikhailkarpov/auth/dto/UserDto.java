package com.mikhailkarpov.auth.dto;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserDto {

    private UUID id;

    private String username;

    private Set<String> roles;
}
