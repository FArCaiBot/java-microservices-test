package com.farcai.ms_clientes.exception;

import java.time.Instant;

public record ApiError(
        String code,
        String message,
        Instant timestamp) {

}
