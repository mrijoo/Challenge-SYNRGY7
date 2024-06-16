package com.binar.gateway.filter;

import com.binar.gateway.client.dto.JwtValidationDto;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    private final WebClient.Builder webClientBuilder;

    public AuthenticationFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    public static class Config {
        // Put configuration properties here if needed
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (RouteValidator.getIsSecured().test(exchange.getRequest())) {
                return validateAuthorizationHeader(exchange, chain);
            }
            return chain.filter(exchange);
        };
    }

    private Mono<Void> validateAuthorizationHeader(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!exchange.getRequest().getHeaders().containsKey("Authorization")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        List<String> authorizationHeaders = exchange.getRequest().getHeaders().get("Authorization");
        String token = authorizationHeaders != null && !authorizationHeaders.isEmpty()
                ? authorizationHeaders.get(0).replace("Bearer ", "")
                : null;

        if (token != null) {
            return validateTokenWithSecurityService(token, exchange, chain);
        } else {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private Mono<Void> validateTokenWithSecurityService(String token, ServerWebExchange exchange,
            GatewayFilterChain chain) {
        return webClientBuilder.build()
                .post()
                .uri("http://security-service/auth/validate-token")
                .bodyValue(JwtValidationDto.builder().token(token).build())
                .exchangeToMono(clientResponse -> handleClientResponse(clientResponse, exchange, chain))
                .onErrorResume(e -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
    }

    private Mono<Void> handleClientResponse(ClientResponse clientResponse, ServerWebExchange exchange,
            GatewayFilterChain chain) {
        if (clientResponse.statusCode().is2xxSuccessful()) {
            return clientResponse.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
            })
                    .flatMap(response -> {
                        Map<String, Object> data = (Map<String, Object>) response.get("data");
                        List<String> roles = (List<String>) data.get("roles");
                        String userId = (String) data.get("user_id");

                        if (isRoleUnauthorized(roles, exchange)) {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        }

                        exchange.getRequest().mutate().header("user_id", userId);

                        return chain.filter(exchange);
                    });
        } else {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private boolean isRoleUnauthorized(List<String> roles, ServerWebExchange exchange) {
        return (RouteValidator.isUserApi().test(exchange.getRequest()) && !roles.contains("USER")) ||
                (RouteValidator.isMerchantApi().test(exchange.getRequest()) && !roles.contains("MERCHANT")) ||
                (RouteValidator.isAdminApi().test(exchange.getRequest()) && !roles.contains("ADMIN"));
    }
}
