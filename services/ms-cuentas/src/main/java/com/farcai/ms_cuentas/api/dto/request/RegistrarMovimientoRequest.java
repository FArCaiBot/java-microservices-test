package com.farcai.ms_cuentas.api.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record RegistrarMovimientoRequest(@NotNull String numeroCuenta,
        @NotNull BigDecimal valor,
        LocalDateTime fecha) {

}
