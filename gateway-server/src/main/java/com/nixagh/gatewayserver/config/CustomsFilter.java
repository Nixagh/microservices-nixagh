package com.nixagh.gatewayserver.config;

import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Component
public class CustomsFilter implements GatewayFilter {

    private final RouterValidator routerValidator;
    private final JwtUtil jwtUtil;

    public CustomsFilter(RouterValidator routerValidator, JwtUtil jwtUtil) {
        this.routerValidator = routerValidator;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String token = null;
        if (routerValidator.isSecured.test(request)) {
            if (this.isAuthMissing(request))
                return this.onError(exchange, "Authorization header is missing in request", HttpStatus.BAD_REQUEST);

            token = this.getAuthHeader(request);
            System.out.println(token);
            if (jwtUtil.isInvalid(token))
                return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);

            this.populateRequestWithHeaders(exchange, token);
        }

        String username = jwtUtil.extractUsername(token);
        String authorities = jwtUtil.extractAuthorities(token);

        exchange.getRequest().mutate().header("username", username);
        exchange.getRequest().mutate().header("authorities", authorities);

        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private String getAuthHeader(ServerHttpRequest request) {
        String token = request.getHeaders().getOrEmpty("Authorization").get(0);
        return token.replace("Bearer ", "");
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, String token) {
        Optional<Claims> claims = jwtUtil.getAllClaimsFromToken(token);
        if (claims.isEmpty()) return;
        Claims claims_ = claims.get();
        exchange.getRequest().mutate()
                .header("id", String.valueOf(claims_.get("id")))
                .header("role", String.valueOf(claims_.get("role")))
                .build();
    }
}
