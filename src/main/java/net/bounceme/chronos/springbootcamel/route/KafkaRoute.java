package net.bounceme.chronos.springbootcamel.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class KafkaRoute extends RouteBuilder {

    @Override
    public void configure() {
        from("kafka:{{app.kafka.topic}}")
                .routeId("kafka-consumer-route")
                .log("Mensaje Kafka recibido: ${body}");
    }
}
