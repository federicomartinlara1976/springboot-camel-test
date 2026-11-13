package com.example.springbootcamel.service;

import com.example.springbootcamel.dto.EchoResponse;
import com.example.springbootcamel.dto.Greeting;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GreetingService {

    private final String greeting;

    public GreetingService(@Value("${app.greeting}") String greeting) {
        this.greeting = greeting;
    }

    public Greeting greet() {
        return Greeting.of(greeting);
    }

    public EchoResponse echo(Map<String, Object> payload) {
        String message = String.valueOf(payload.getOrDefault("message", ""));
        return new EchoResponse(message, message.length());
    }
}
