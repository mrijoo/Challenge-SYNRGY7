package com.binar.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.binar.gateway.filter.AuthenticationFilter;

@Configuration
public class SpringCloudConfig {
        @Bean
        public RouteLocator gatewayRoutes(RouteLocatorBuilder builder, AuthenticationFilter authFilter) {
                return builder.routes()
                                .route("security-service", r -> r.path("/auth/**")
                                                .uri("lb://security-service"))
                                .route("binarfud-service", r -> r.path("/binarfud/**")
                                                .filters(f -> f.filter(
                                                                authFilter.apply(new AuthenticationFilter.Config())))
                                                .uri("lb://binarfud-service"))
                                .route("chat-service", r -> r.path("/chat/**")
                                                .filters(f -> f.filter(
                                                                authFilter.apply(new AuthenticationFilter.Config())))
                                                .uri("lb://chat-service"))
                                .build();
        }
}
