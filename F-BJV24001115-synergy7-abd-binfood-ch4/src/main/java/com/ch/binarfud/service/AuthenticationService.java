package com.ch.binarfud.service;

import com.ch.binarfud.dto.auth.LoginDto;
import com.ch.binarfud.dto.auth.RegisterDto;
import com.ch.binarfud.model.User;

public interface AuthenticationService {
    public User signup(RegisterDto input);

    public User authenticate(LoginDto input);
}
