package com.nixagh.userservice.commons;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetAuthorization {
    public static List<String> getAuthorization() {
        // fetch authorization from database or cache
        return List.of("ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER_ADMIN");
    }

    public static List<String> getAuthorities(String path) {
        // fetch authorization from database or cache
        return List.of("ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER_ADMIN");
    }
}
