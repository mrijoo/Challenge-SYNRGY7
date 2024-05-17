package com.ch.binarfud.controller;

import org.springframework.web.bind.annotation.RestController;

import com.ch.binarfud.dto.user.request.UserDto;
import com.ch.binarfud.dto.user.response.UserResponseDto;
import com.ch.binarfud.service.UserService;
import com.ch.binarfud.util.ApiResponse;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllUser(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<UserResponseDto> usersPage = userService.getAllUser(page, size);
        return ApiResponse.success(HttpStatus.OK, "Users has successfully retrieved",
                usersPage.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable UUID id) {
        UserResponseDto user = userService.getUserById(id);
        return ApiResponse.success(HttpStatus.OK, "User has successfully retrieved", user);
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@Valid @RequestBody UserDto userDto) {
        UserResponseDto user = userService.addUser(userDto);
        return ApiResponse.success(HttpStatus.CREATED, "User has successfully added", user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable UUID id, @Valid @RequestBody UserDto userDto) {
        UserResponseDto user = userService.updateUser(id, userDto);
        return ApiResponse.success(HttpStatus.OK, "User has successfully updated", user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ApiResponse.success(HttpStatus.OK, "User has successfully deleted");
    }
}
