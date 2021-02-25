package com.mikhailkarpov.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUpdateUserRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotNull
    @NotEmpty
    private Set<String> roles;

    @Override
    public String toString() {
        return "CreateUpdateUserRequest{" +
                "username='" + username + '\'' +
                ", password='" + "PROTECTED" + '\'' +
                ", roles=" + roles +
                '}';
    }
}
