package com.farcai.ms_cuentas.messaging.dto;

import java.time.Instant;

public record ClienteEventMessage(String eventId,
        String eventType,
        Instant occurredAt,
        int version,
        Data data) {

    public record Data(
            Long clienteId,
            String nombre,
            String identificacion,
            Boolean estado) {
    }

}

