package com.ch.binarfud.service;

import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ch.binarfud.dto.user.request.UserDto;
import com.ch.binarfud.dto.user.response.UserResponseDto;
import com.ch.binarfud.exception.DataNotFoundException;
import com.ch.binarfud.model.User;
import com.ch.binarfud.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private ModelMapper modelMapper;

    private final UserRepository userRepository;

    public UserServiceImpl(ModelMapper modelMapper, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public Page<UserResponseDto> getAllUser(int page, int size) {
        Page<User> users = userRepository.findAll(PageRequest.of(page, size));
        return users.map(user -> modelMapper.map(user, UserResponseDto.class));
    }

    @Override
    public UserResponseDto getUserById(UUID id) {
        User existingUser = userRepository.findUserById(id)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        return modelMapper.map(existingUser, UserResponseDto.class);
    }

    @Override
    public UserResponseDto addUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserResponseDto.class);
    }

    @Override
    public UserResponseDto updateUser(UUID id, UserDto userDto) {
        User existingUser = userRepository.findUserById(id)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        modelMapper.map(userDto, existingUser);
        existingUser.setId(id);

        User updatedUser = userRepository.save(existingUser);

        return modelMapper.map(updatedUser, UserResponseDto.class);
    }

    @Override
    public void deleteUser(UUID id) {
        User existingUser = userRepository.findUserById(id)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        userRepository.delete(existingUser);
    }

    @Override
    public User getUserByUsername(String name) {
        return userRepository.findByUsername(name)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
    }

    @Override
    public void transferBalance(UUID senderId, UUID receiverId, double amount) {
        userRepository.findUserById(senderId)
                .orElseThrow(() -> new DataNotFoundException("Sender not found"));
        userRepository.findUserById(receiverId)
                .orElseThrow(() -> new DataNotFoundException("Receiver not found"));

        userRepository.transferBalance(senderId, receiverId, amount);
    }
}
