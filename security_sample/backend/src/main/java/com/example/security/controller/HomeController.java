package com.example.security.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HomeController {

    @GetMapping("/home")
    public Map<String, Object> home(Authentication authentication) {
        return Map.of(
                "message", "Welcome to the protected dashboard, " + authentication.getName() + "!",
                "username", authentication.getName(),
                "authorities", authentication.getAuthorities(),
                "timestamp", Instant.now().toString()
        );
    }
}
