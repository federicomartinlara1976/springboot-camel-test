package com.example.springbootcamel.route;

import com.example.springbootcamel.SpringbootCamelApplication;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@CamelSpringBootTest
@SpringBootTest(classes = SpringbootCamelApplication.class, properties = {
        "server.port=0",
        "camel.springboot.java-routes-exclude-pattern=rest-*"
})
class CamelRoutesIntegrationTest {

    @Autowired
    private ProducerTemplate template;

    @Test
    void greetRouteReturnsJson() {
        String result = template.requestBody("direct:greet", null, String.class);
        assertNotNull(result);
        assertTrue(result.contains("Hola desde Apache Camel"));
    }

    @Test
    void echoRouteReturnsJson() {
        String result = template.requestBody("direct:echo", "{\"message\":\"hola\"}", String.class);
        assertNotNull(result);
        assertTrue(result.contains("hola"));
        assertTrue(result.contains("length"));
    }
}
