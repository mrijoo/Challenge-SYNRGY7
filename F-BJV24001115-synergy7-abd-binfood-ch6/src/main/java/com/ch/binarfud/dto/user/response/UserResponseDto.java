package com.ch.binarfud.dto.user.response;

import java.util.UUID;

import lombok.Data;

@Data
public class UserResponseDto {
    private UUID id;

    private String username;

    private String email;
}
