package com.ch.binarfud.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
        private final AuthenticationProvider authenticationProvider;
        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final JwtAuthenticationLogout jwtAuthenticationLogout;

        private final AuthEntryPointJwt authEntryPointJwt;
        private final OauthSuccessHandler oauthSuccessHandler;

        public WebSecurityConfig(
                        JwtAuthenticationFilter jwtAuthenticationFilter,
                        AuthenticationProvider authenticationProvider,
                        JwtAuthenticationLogout jwtAuthenticationLogout,
                        AuthEntryPointJwt authEntryPointJwt,
                        OauthSuccessHandler oauthSuccessHandler) {
                this.authenticationProvider = authenticationProvider;
                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
                this.jwtAuthenticationLogout = jwtAuthenticationLogout;
                this.authEntryPointJwt = authEntryPointJwt;
                this.oauthSuccessHandler = oauthSuccessHandler;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                OidcUserService oidcUserService = new OidcUserService();
                return http.csrf(AbstractHttpConfigurer::disable)
                                .cors(Customizer.withDefaults())
                                .exceptionHandling(exception -> exception
                                                .authenticationEntryPoint(authEntryPointJwt))
                                .authorizeHttpRequests(requests -> requests
                                                .requestMatchers("/auth/**").permitAll()
                                                .requestMatchers("/v3/api-docs/**",
                                                                "/swagger*/**")
                                                .permitAll()
                                                .requestMatchers("/login").permitAll()
                                                .requestMatchers("/api/v1/products", "/api/v1/products/*").permitAll()
                                                .anyRequest()
                                                .authenticated())
                                .sessionManagement(management -> management
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                                .logout(logout -> logout
                                                .logoutUrl("/auth/signout")
                                                .addLogoutHandler(jwtAuthenticationLogout)
                                                .logoutSuccessHandler((request, response, authentication) -> {
                                                        SecurityContextHolder.clearContext();
                                                        response.setStatus(HttpServletResponse.SC_OK);
                                                }))
                                .oauth2Login(oauth2 -> oauth2
                                                .loginPage("/login")
                                                .userInfoEndpoint(userInfo -> userInfo
                                                                .oidcUserService(oidcUserService))
                                                .successHandler(oauthSuccessHandler))
                                .build();
        }
}
