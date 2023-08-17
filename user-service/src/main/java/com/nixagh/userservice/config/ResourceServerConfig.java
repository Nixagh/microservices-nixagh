//package com.nixagh.userservice.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
//import org.springframework.security.web.SecurityFilterChain;

//@EnableWebSecurity
//public class ResourceServerConfig {
//
//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(request ->
//                request.anyRequest().hasAuthority("SCOPE_message:read")
//        ).oauth2ResourceServer(oauth2 ->
//                oauth2.jwt(jwt ->
//                        jwt.jwtAuthenticationConverter(new JwtAuthenticationConverter()))
//        );
//        return http.build();
//    }
//}