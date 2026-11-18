package net.bounceme.chronos.springbootcamel.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
	
	@Bean
    ConnectionFactory rabbitConnectionFactory(
            @Value("${app.rabbitmq.host}") String host,
            @Value("${app.rabbitmq.port}") int port,
            @Value("${app.rabbitmq.username}") String username,
            @Value("${app.rabbitmq.password}") String password) {
        CachingConnectionFactory factory = new CachingConnectionFactory(host, port);
        factory.setUsername(username);
        factory.setPassword(password);
        return factory;
    }
}
