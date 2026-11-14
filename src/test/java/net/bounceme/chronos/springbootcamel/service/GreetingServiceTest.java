package net.bounceme.chronos.springbootcamel.service;

import org.junit.jupiter.api.Test;

import net.bounceme.chronos.springbootcamel.dto.EchoResponse;
import net.bounceme.chronos.springbootcamel.dto.Greeting;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GreetingServiceTest {

    private final GreetingService service = new GreetingService("Hola test");

    @Test
    void greetReturnsConfiguredGreeting() {
        Greeting greeting = service.greet();
        assertEquals("Hola test", greeting.message());
        assertEquals(36, greeting.timestamp().length());
    }

    @Test
    void echoReturnsMessageAndLength() {
        EchoResponse response = service.echo(Map.of("message", "hola mundo"));
        assertEquals("hola mundo", response.message());
        assertEquals(10, response.length());
    }

    @Test
    void echoHandlesMissingMessage() {
        EchoResponse response = service.echo(Map.of());
        assertEquals("null", response.message());
        assertTrue(response.length() >= 0);
    }
}
