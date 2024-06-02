package com.ch.binarfud.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.ch.binarfud.dto.user.request.UserDto;
import com.ch.binarfud.dto.user.response.UserResponseDto;
import com.ch.binarfud.model.User;

public interface UserService {
    Page<UserResponseDto> getAllUser(int page, int size);

    UserResponseDto getUserById(UUID id);
    
    UserResponseDto addUser(UserDto userDto);
    
    UserResponseDto updateUser(UUID id, UserDto userDto);
    
    void deleteUser(UUID id);
    
    User getUserByUsername(String name);

    void transferBalance(UUID senderId, UUID receiverId, double amount);
}
