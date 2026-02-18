package com.farcai.ms_cuentas.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistrarMovimientoRequest(@NotNull @Size(max = 10) String numeroCuenta,
        @NotNull BigDecimal valor,
        LocalDateTime fecha) {

}

