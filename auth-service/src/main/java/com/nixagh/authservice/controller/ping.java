package com.nixagh.authservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class ping {

    @Value("${db.name:Data null}")
    private String dbName;

    @GetMapping("/ping")
    public String getPing(HttpServletRequest request) {
        String x = request.getHeader("Authorization1");
        return "pong " + x + " " + dbName;
    }
}
