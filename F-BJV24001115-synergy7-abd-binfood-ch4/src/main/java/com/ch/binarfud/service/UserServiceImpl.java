package com.ch.binarfud.service;

import com.ch.binarfud.model.User;
import com.ch.binarfud.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<User> getAllUsers(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }

    @Override
    public ResponseEntity<User> getProfileUser(User user) {
        return ResponseEntity.ok(user);
    }

    @Override
    public User createUser(User user) {
        userRepository.addUser(user.getUsername(), user.getEmail(), user.getPassword());
        return userRepository.findByUsername(user.getUsername()).orElse(null);
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User updateUser(UUID id, User user) {
        userRepository.updateUser(id, user.getUsername(), user.getEmail(), user.getPassword());
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteUserById(id);
    }
}
