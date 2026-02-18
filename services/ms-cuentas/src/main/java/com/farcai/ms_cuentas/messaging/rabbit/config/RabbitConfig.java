package com.farcai.ms_cuentas.messaging.rabbit.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String CLIENTES_EXCHANGE = "clientes.events";
    public static final String SNAPSHOT_QUEUE = "cuentas.clientes.snapshot";

    @Bean
    TopicExchange clientesExchange() {
        return new TopicExchange(CLIENTES_EXCHANGE, true, false);
    }

    @Bean
    Queue snapshotQueue() {
        return QueueBuilder.durable(SNAPSHOT_QUEUE).build();
    }

    @Bean
    Binding bindingCreated(Queue snapshotQueue, TopicExchange clientesExchange) {
        return BindingBuilder.bind(snapshotQueue).to(clientesExchange).with("clientes.created");
    }

    @Bean
    Binding bindingUpdated(Queue snapshotQueue, TopicExchange clientesExchange) {
        return BindingBuilder.bind(snapshotQueue).to(clientesExchange).with("clientes.updated");
    }

    @Bean
    Binding bindingDisabled(Queue snapshotQueue, TopicExchange clientesExchange) {
        return BindingBuilder.bind(snapshotQueue).to(clientesExchange).with("clientes.disabled");
    }

    @Bean
    Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}

