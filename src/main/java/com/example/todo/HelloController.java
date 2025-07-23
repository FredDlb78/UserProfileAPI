package com.example.todo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public Map<String, String> sayHello() {
        // ✅ Good practice: return a Map or a DTO, Spring Boot fera le JSON automatiquement
        return Map.of("message", "Hello from my first API 🚀");
    }
}
