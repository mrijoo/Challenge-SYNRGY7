package com.binar.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
        private final JwtAuthenticationLogout jwtAuthenticationLogout;

        private final AuthEntryPointJwt authEntryPointJwt;

        private final OauthSuccessHandler oauthSuccessHandler;

        public WebSecurityConfig(
                        JwtAuthenticationLogout jwtAuthenticationLogout,
                        AuthEntryPointJwt authEntryPointJwt,
                        OauthSuccessHandler oauthSuccessHandler) {
                this.jwtAuthenticationLogout = jwtAuthenticationLogout;
                this.authEntryPointJwt = authEntryPointJwt;
                this.oauthSuccessHandler = oauthSuccessHandler;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                OidcUserService oidcUserService = new OidcUserService();
                return http.csrf(csrf -> csrf.disable())
                                .exceptionHandling(exception -> exception
                                                .authenticationEntryPoint(authEntryPointJwt))
                                .authorizeHttpRequests(requests -> requests
                                                .anyRequest().permitAll())
                                .oauth2Login(oauth2 -> oauth2
                                                .loginPage("/login")
                                                .userInfoEndpoint(userInfo -> userInfo
                                                                .oidcUserService(oidcUserService))
                                                .successHandler(oauthSuccessHandler))
                                .logout(logout -> logout
                                                .logoutUrl("/auth/signout")
                                                .addLogoutHandler(jwtAuthenticationLogout)
                                                .logoutSuccessHandler((request, response, authentication) -> {
                                                        SecurityContextHolder.clearContext();
                                                        response.setStatus(HttpServletResponse.SC_OK);
                                                }))
                                .build();
        }

}
