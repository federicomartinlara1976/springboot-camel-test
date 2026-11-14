package net.bounceme.chronos.springbootcamel.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import net.bounceme.chronos.springbootcamel.service.GreetingService;

@Component
public class GreetingRoute extends RouteBuilder {

    private final GreetingService greetingService;

    public GreetingRoute(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @Override
    public void configure() {
        rest("/api")
                .get("/hello")
                .id("rest-hello")
                .produces("application/json")
                .to("direct:greet")

                .post("/echo")
                .id("rest-echo")
                .consumes("application/json")
                .produces("application/json")
                .to("direct:echo")

                .post("/messages")
                .id("rest-publish")
                .consumes("application/json")
                .to("direct:publish-message");

        from("direct:greet")
                .routeId("greet-route")
                .bean(greetingService, "greet")
                .marshal().json();

        from("direct:echo")
                .routeId("echo-route")
                .unmarshal().json()
                .bean(greetingService, "echo")
                .marshal().json();

        from("direct:publish-message")
                .routeId("publish-message-route")
                .log("Publicando mensaje en Kafka: ${body}")
                .to("kafka:{{app.kafka.topic}}")
                .setBody(constant("{\"status\":\"publicado\"}"));
    }
}
