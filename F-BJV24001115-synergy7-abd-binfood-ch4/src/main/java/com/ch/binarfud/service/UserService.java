package com.ch.binarfud.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.ch.binarfud.model.User;

public interface UserService {
    Page<User> getAllUsers(int page, int size);

    ResponseEntity<User> getProfileUser(User user);

    User getUserById(UUID id);

    User createUser(User user);

    User updateUser(UUID id, User user);

    void deleteUser(UUID id);
}
