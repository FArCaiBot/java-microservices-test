package com.farcai.ms_cuentas.infrastructure.messaging.rabbit.consumer;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.farcai.ms_cuentas.infrastructure.messaging.dto.ClienteEventMessage;
import com.farcai.ms_cuentas.infrastructure.messaging.rabbit.config.RabbitConfig;
import com.farcai.ms_cuentas.infrastructure.persistence.jpa.entity.ClienteSnapshotEntity;
import com.farcai.ms_cuentas.infrastructure.persistence.jpa.entity.ProcessedEventEntity;
import com.farcai.ms_cuentas.infrastructure.persistence.jpa.repository.ClienteSnapshotJpaRepository;
import com.farcai.ms_cuentas.infrastructure.persistence.jpa.repository.ProcessedEventJpaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClienteEventsConsumer {

    private final ClienteSnapshotJpaRepository snapshotRepo;
    private final ProcessedEventJpaRepository processedRepo;

    @RabbitListener(queues = RabbitConfig.SNAPSHOT_QUEUE)
    @Transactional
    public void handle(ClienteEventMessage msg) {

        // idempotencia: si ya procesamos eventId -> salir
        if (processedRepo.existsById(msg.eventId())) {
            return;
        }

        var data = msg.data();

        ClienteSnapshotEntity snapshot = new ClienteSnapshotEntity(
                data.clienteId(),
                data.nombre(),
                data.identificacion(),
                Boolean.TRUE.equals(data.estado()),
                LocalDateTime.now());

        // upsert (save sobre PK)
        snapshotRepo.save(snapshot);

        // marcar evento como procesado
        processedRepo.save(new ProcessedEventEntity(msg.eventId(), LocalDateTime.now()));
    }
}