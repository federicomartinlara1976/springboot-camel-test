package net.bounceme.chronos.springbootcamel.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqRoute extends RouteBuilder {

    @Override
    public void configure() {
    	from("spring-rabbitmq:camel.exchange?queues=test&routingKey=camel.key&autoDeclare=true")
        	.routeId("rabbitmq-consumer-route")
        	.log("Mensaje RabbitMQ recibido: ${body}");
    }
}
