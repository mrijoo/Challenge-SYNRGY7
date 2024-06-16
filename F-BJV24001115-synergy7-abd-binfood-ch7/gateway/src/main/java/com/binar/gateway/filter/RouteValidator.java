package com.binar.gateway.filter;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RouteValidator {
    private RouteValidator() {
    }

    private static final List<String> openApiEndpoints = List.of(
            "_public",
            "/auth/**",
            "/eureka");

    public static Predicate<ServerHttpRequest> getIsSecured() {
        return request -> openApiEndpoints
                .stream()
                .noneMatch(uri -> request.getURI().getPath().contains(uri));
    }

    public static Predicate<ServerHttpRequest> isUserApi() {
        return request -> request.getURI().getPath().contains("/_user");
    }

    public static Predicate<ServerHttpRequest> isMerchantApi() {
        return request -> request.getURI().getPath().contains("/_merchant");
    }

    public static Predicate<ServerHttpRequest> isAdminApi() {
        return request -> request.getURI().getPath().contains("/_admin");
    }
}
