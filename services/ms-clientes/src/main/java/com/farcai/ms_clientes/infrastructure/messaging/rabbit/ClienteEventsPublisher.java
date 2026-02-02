package com.farcai.ms_clientes.infrastructure.messaging.rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.farcai.ms_clientes.infrastructure.messaging.dto.ClienteEventMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClienteEventsPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(String routingKey, ClienteEventMessage message) {
        rabbitTemplate.convertAndSend(RabbitConfig.CLIENTES_EXCHANGE, routingKey, message);
    }

}
