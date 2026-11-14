package net.bounceme.chronos.springbootcamel.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.bounceme.chronos.springbootcamel.dto.EchoResponse;
import net.bounceme.chronos.springbootcamel.dto.Greeting;

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
