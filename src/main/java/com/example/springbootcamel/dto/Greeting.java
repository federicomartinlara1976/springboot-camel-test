package com.example.springbootcamel.dto;

import java.time.Instant;

public record Greeting(String message, String timestamp) {

    public static Greeting of(String message) {
        return new Greeting(message, Instant.now().toString());
    }
}
