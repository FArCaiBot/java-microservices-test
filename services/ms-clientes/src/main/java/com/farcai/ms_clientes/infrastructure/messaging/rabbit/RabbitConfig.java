package com.farcai.ms_clientes.infrastructure.messaging.rabbit;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String CLIENTES_EXCHANGE = "clientes.events";

    @Bean
    TopicExchange clientesExchange() {
        return new TopicExchange(CLIENTES_EXCHANGE, true, false);
    }

    @Bean
    Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
