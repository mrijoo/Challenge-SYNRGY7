package com.binar.security.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.binar.security.dto.account.detail.AccountDetailResponseDto;
import com.binar.security.dto.account.identity.AddIdentityDto;
import com.binar.security.dto.account.identity.IdentityResponseDto;
import com.binar.security.exception.DataNotFoundException;
import com.binar.security.model.UserIdentity;
import com.binar.security.repository.UserIdentityRepository;
import com.binar.security.repository.UserRepository;

@Service
public class AccountServiceImpl implements AccountService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final UserIdentityRepository userIdentityRepository;

    public AccountServiceImpl(ModelMapper modelMapper, UserRepository userRepository,
            UserIdentityRepository userIdentityRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.userIdentityRepository = userIdentityRepository;
    }

    @Override
    public AccountDetailResponseDto getAccountDetail(UUID userId) {
        return userRepository.findById(userId)
                .map(user -> modelMapper.map(user, AccountDetailResponseDto.class))
                .orElseThrow(() -> new DataNotFoundException("User not found"));
    }

    @Override
    public IdentityResponseDto addUserIdentity(AddIdentityDto addIdentityDto) {
        System.out.println(userIdentityRepository.findByUserId(addIdentityDto.getUserId()));
        if (userIdentityRepository.findByUserId(addIdentityDto.getUserId()) != null) {
            throw new IllegalStateException("User identity already exists");
        }
        UserIdentity userIdentity = modelMapper.map(addIdentityDto, UserIdentity.class);
        userIdentity.setUser(userRepository.findById(addIdentityDto.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User not found")));
        userIdentity = userIdentityRepository.save(userIdentity);
        return modelMapper.map(userIdentity, IdentityResponseDto.class);
    }

}
