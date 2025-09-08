package com.tacocloud.messaging;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("amqp-producer")
@Configuration
public class AmqpMessageConfig {
    /**
     * Configures a message converter for RabbitMQ that converts messages to JSON format.
     */
    @Profile("rabbitmq-producer")
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

