package com.binar.security.service;

import java.util.UUID;

import com.binar.security.dto.account.detail.AccountDetailResponseDto;
import com.binar.security.dto.account.identity.AddIdentityDto;
import com.binar.security.dto.account.identity.IdentityResponseDto;

public interface AccountService {
    public AccountDetailResponseDto getAccountDetail(UUID userId);

    public IdentityResponseDto addUserIdentity(AddIdentityDto addIdentityDto);
}
