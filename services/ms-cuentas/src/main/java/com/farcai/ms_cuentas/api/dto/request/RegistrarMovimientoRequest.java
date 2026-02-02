package com.farcai.ms_cuentas.api.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.farcai.ms_cuentas.domain.model.TipoMovimiento;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RegistrarMovimientoRequest(@NotNull Long cuentaId,
        @NotNull TipoMovimiento tipo,
        @NotNull @Positive BigDecimal valor,
        LocalDateTime fecha) {

}
