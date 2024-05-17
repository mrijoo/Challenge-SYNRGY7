package com.ch.binarfud.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ch.binarfud.dto.auth.refresh.request.RefreshTokenDto;
import com.ch.binarfud.dto.auth.refresh.response.RefreshTokenResponseDto;
import com.ch.binarfud.dto.auth.signin.request.SigninDto;
import com.ch.binarfud.dto.auth.signin.response.SigninResponseDto;
import com.ch.binarfud.dto.auth.signup.request.SignupDto;
import com.ch.binarfud.dto.auth.signup.response.SignupResponseDto;
import com.ch.binarfud.model.User;
import com.ch.binarfud.repository.UserRepository;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public AuthenticationServiceImpl(
            ModelMapper modelMapper,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @Override
    public SigninResponseDto signin(SigninDto signinDto) {
        String username = signinDto.getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        boolean isUsername = true;

        if (!user.isPresent() && !username.contains("@")) {
            user = userRepository.findByEmail(username);
            isUsername = false;
        }

        if (user.isPresent()) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.get().getUsername(),
                            signinDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            SigninResponseDto responseSigninDto = modelMapper.map(user.get(), SigninResponseDto.class);
            responseSigninDto.setAccessToken(jwtService.generateToken(user.get()));
            responseSigninDto.setRefreshToken(jwtService.generateRefreshToken(user.get()));

            return responseSigninDto;
        } else {
            String errorMessage;

            if (!isUsername) {
                errorMessage = "Username or password is incorrect.";
            } else {
                errorMessage = "Email or password is incorrect.";
            }
            throw new UsernameNotFoundException(errorMessage);
        }

    }

    @Override
    public SignupResponseDto signup(SignupDto signupDto) {
        User user = modelMapper.map(signupDto, User.class);
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));

        User savedUser = userRepository.save(user);

        SignupResponseDto signupResponseDto = modelMapper.map(savedUser, SignupResponseDto.class);
        signupResponseDto.setAccessToken(jwtService.generateToken(savedUser));
        signupResponseDto.setRefreshToken(jwtService.generateRefreshToken(savedUser));

        return signupResponseDto;
    }

    @Override
    public RefreshTokenResponseDto refreshToken(RefreshTokenDto refreshTokenDto) {
        String refreshToken = refreshTokenDto.getRefreshToken();
        String username = jwtService.extractUsername(refreshToken);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (jwtService.isTokenValid(refreshToken, user)) {
            RefreshTokenResponseDto refreshTokenResponseDto = new RefreshTokenResponseDto();
            refreshTokenResponseDto.setAccessToken(jwtService.generateToken(user));
            refreshTokenResponseDto.setRefreshToken(jwtService.generateRefreshToken(user));

            return refreshTokenResponseDto;
        } else {
            throw new UsernameNotFoundException("Invalid refresh token");
        }
    }
}
