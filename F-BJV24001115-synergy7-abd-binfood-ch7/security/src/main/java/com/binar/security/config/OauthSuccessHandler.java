package com.binar.security.config;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.binar.security.enums.UserRole;
import com.binar.security.model.User;
import com.binar.security.repository.UserRepository;
import com.binar.security.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OauthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public OauthSuccessHandler(JwtService jwtService, UserRepository userRepository, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

        String email = oidcUser.getEmail();
        Optional<User> userOptional = userRepository.findByEmail(email);

        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            user = new User();
            user.setEmail(email);
            user.setUsername(email);
            user.setRoles(Set.of(UserRole.USER));
            user.setEnabled(true);
            userRepository.save(user);
        }

        String[] tokens = jwtService.generateToken(user);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        var responseMap = new LinkedHashMap<String, Object>();
        responseMap.put("username", user.getUsername());
        responseMap.put("email", user.getEmail());
        responseMap.put("accessToken", tokens[0]);
        responseMap.put("refreshToken", tokens[1]);
        responseMap.put("roles", user.getRoles().stream()
                .map(UserRole::toString)
                .collect(Collectors.toList()));

        response.getWriter().write(objectMapper.writeValueAsString(responseMap));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
