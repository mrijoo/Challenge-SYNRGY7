package com.ch.binarfud.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ch.binarfud.dto.auth.forgot.request.ForgotPasswordDto;
import com.ch.binarfud.dto.auth.forgot.request.ForgotPasswordRequestDto;
import com.ch.binarfud.dto.auth.refresh.request.RefreshTokenDto;
import com.ch.binarfud.dto.auth.refresh.response.RefreshTokenResponseDto;
import com.ch.binarfud.dto.auth.signin.request.SigninDto;
import com.ch.binarfud.dto.auth.signin.response.SigninResponseDto;
import com.ch.binarfud.dto.auth.signup.request.ResendVerificationEmailDto;
import com.ch.binarfud.dto.auth.signup.request.SignupDto;
import com.ch.binarfud.dto.auth.signup.request.VerificationEmailDto;
import com.ch.binarfud.dto.auth.signup.response.SignupResponseDto;
import com.ch.binarfud.model.Role;
import com.ch.binarfud.model.User;
import com.ch.binarfud.model.Otp;
import com.ch.binarfud.repository.UserRepository;
import com.ch.binarfud.repository.OtpRepository;
import com.ch.binarfud.util.HashUtil;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final MailService mailService;

    public AuthenticationServiceImpl(
            ModelMapper modelMapper,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            OtpRepository otpRepository,
            MailService mailService) {
        this.modelMapper = modelMapper;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.mailService = mailService;
    }

    private Random random = new Random();

    private Otp createOtp(User user, Otp.Type type) {
        int code = random.nextInt(90000000) + 10000000;

        Otp otp = new Otp();
        otp.setCode(HashUtil.hashCode(String.valueOf(code)));
        otp.setUser(user);
        otp.setExpiryDate(LocalDateTime.now().plusMinutes(5));
        otp.setType(type);
        otpRepository.save(otp);

        return new Otp(user, String.valueOf(code), otp.getExpiryDate());
    }

    @Override
    public SigninResponseDto signin(SigninDto signinDto) {
        String username = signinDto.getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        boolean isUsername = true;

        if (!user.isPresent() && username.contains("@")) {
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
            String[] tokens = jwtService.generateToken(user.get());
            responseSigninDto.setAccessToken(tokens[0]);
            responseSigninDto.setRefreshToken(tokens[1]);

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
        if (!signupDto.getPassword().equals(signupDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and confirm password must be the same");
        }

        User user = modelMapper.map(signupDto, User.class);
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        user.setRoles(Set.of(Role.Roles.USER));

        User savedUser = userRepository.save(user);

        Otp otp = createOtp(savedUser, Otp.Type.REGISTRATION);

        mailService.sendVerificationEmail(savedUser.getEmail(), savedUser.getUsername(), otp.getCode());

        SignupResponseDto signupResponseDto = modelMapper.map(savedUser, SignupResponseDto.class);
        String[] tokens = jwtService.generateToken(savedUser);
        signupResponseDto.setAccessToken(tokens[0]);
        signupResponseDto.setRefreshToken(tokens[1]);

        return signupResponseDto;
    }

    @Override
    public void resendVerificationEmail(ResendVerificationEmailDto resendEmailDto) {
        User user = userRepository.findByEmail(resendEmailDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.isEnabled()) {
            throw new IllegalArgumentException("User already verified");
        }

        otpRepository.findByUser(user).ifPresent(otpRepository::delete);

        Otp otp = createOtp(user, Otp.Type.REGISTRATION);

        mailService.sendVerificationEmail(user.getEmail(), user.getUsername(), otp.getCode());
    }

    @Override
    public void verifyEmail(VerificationEmailDto verificationEmailDto) {
        Otp otp = otpRepository.findByCodeAndType(
                HashUtil.hashCode(verificationEmailDto.getOtp()), Otp.Type.REGISTRATION);

        if (otp == null) {
            throw new IllegalArgumentException("Invalid OTP");
        } else if (otp.getExpiryDate().isBefore(LocalDateTime.now())) {
            otpRepository.delete(otp);
            throw new IllegalArgumentException("OTP expired");
        }

        User user = otp.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        otpRepository.delete(otp);
    }

    @Override
    public void forgotPasswordRequest(ForgotPasswordRequestDto forgotPasswordDto) {
        String username = forgotPasswordDto.getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        boolean isUsername = true;

        if (!user.isPresent() && username.contains("@")) {
            user = userRepository.findByEmail(username);
            isUsername = false;
        }

        if (user.isPresent()) {
            Otp otp = createOtp(user.get(), Otp.Type.PASSWORD_RESET);

            mailService.sendResetPasswordEmail(user.get().getEmail(), user.get().getUsername(), otp.getCode());
        } else {
            String errorMessage = isUsername ? "Username not found." : "Email not found.";

            throw new UsernameNotFoundException(errorMessage);
        }
    }

    @Override
    public void forgotPassword(ForgotPasswordDto forgotPasswordDto) {
        Otp otp = otpRepository.findByCodeAndType(
                HashUtil.hashCode(forgotPasswordDto.getOtp()), Otp.Type.PASSWORD_RESET);

        if (otp == null) {
            throw new IllegalArgumentException("Invalid OTP");
        } else if (otp.getExpiryDate().isBefore(LocalDateTime.now())) {
            otpRepository.delete(otp);
            throw new IllegalArgumentException("OTP expired");
        } else if (!forgotPasswordDto.getPassword().equals(forgotPasswordDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and confirm password must be the same");
        }

        User user = otp.getUser();
        user.setPassword(passwordEncoder.encode(forgotPasswordDto.getPassword()));
        userRepository.save(user);
        otpRepository.delete(otp);
    }

    @Override
    public void signout(String token) {
        jwtService.signOut(token);
    }

    @Override
    public RefreshTokenResponseDto refreshToken(RefreshTokenDto refreshTokenDto) {
        String refreshToken = refreshTokenDto.getRefreshToken();
        String username = jwtService.extractRefreshUsername(refreshToken);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (jwtService.isRefreshTokenValid(refreshToken, user)) {
            RefreshTokenResponseDto refreshTokenResponseDto = new RefreshTokenResponseDto();
            String[] tokens = jwtService.generateToken(user, refreshToken);
            refreshTokenResponseDto.setAccessToken(tokens[0]);
            refreshTokenResponseDto.setRefreshToken(tokens[1]);

            return refreshTokenResponseDto;
        } else {
            throw new UsernameNotFoundException("Invalid refresh token");
        }
    }
}