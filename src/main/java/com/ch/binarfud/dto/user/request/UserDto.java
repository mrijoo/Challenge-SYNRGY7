package com.ch.binarfud.dto.user.request;

import com.ch.binarfud.dto.validation.Unique;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDto {
    @NotBlank(message = "username must not be null")
    @Unique(column = "username", message = "username already exists")
    private String username;

    @Email(message = "email not valid")
    @Unique(column = "email", message = "email already exists")
    @NotBlank(message = "email must not be null")
    private String email;

    @NotBlank(message = "password must not be null")
    private String password;
}
