package com.nixagh.gatewayserver.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    public Optional<Claims> getAllClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception ignored) {
        }
        return Optional.ofNullable(claims);
    }

    private boolean isTokenExpired(String token) {
        return this.getAllClaimsFromToken(token)
                .map(Claims::getExpiration)
                .map(expiration -> expiration.before(new Date()))
                .orElse(false);
    }

    public boolean isInvalid(String token) {
        return this.isTokenExpired(token);
    }

    private Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(bytes);
    }

    public String extractUsername(String token) {
        return this.getAllClaimsFromToken(token)
                .map(Claims::getSubject)
                .orElse(null);
    }

    public String extractAuthorities(String token) {
        return this.getAllClaimsFromToken(token)
                .map(claims -> claims.get("authorities", String.class))
                .orElse(null);
    }
}