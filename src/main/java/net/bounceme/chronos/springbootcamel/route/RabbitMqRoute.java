package net.bounceme.chronos.springbootcamel.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqRoute extends RouteBuilder {

    @Override
    public void configure() {
    	from("spring-rabbitmq:camel.exchange?queue=camel.queue&routingKey=camel.key&autoDelete=false")
        	.routeId("rabbitmq-consumer-route")
        	.log("Mensaje RabbitMQ recibido: ${body}");
    }
}
