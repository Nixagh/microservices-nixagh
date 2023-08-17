package com.nixagh.gatewayserver.config;

import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GateWayConfig {

    private final CustomsFilter customsFilter;

    public GateWayConfig(CustomsFilter customsFilter) {
        this.customsFilter = customsFilter;
    }

    // only use for redirect, no authentication and authorization
    // service will be called to auth-service for get token and check token
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        // auth-service
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/auth/**")
                        .uri("lb://auth-service")
                )
                .route("config-server", r -> r
                        .path("/config/**")
                        .uri("lb://config-server")
                )
                .route("user-service", r -> r
                        .path("/user", "/user/**")
                        .filters(f -> f.filter(customsFilter))
                        .uri("lb://user-service")
                )
                .build();
    }
}
