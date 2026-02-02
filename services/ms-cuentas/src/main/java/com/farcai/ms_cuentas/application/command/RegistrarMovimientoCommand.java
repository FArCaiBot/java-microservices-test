package com.farcai.ms_cuentas.application.command;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.farcai.ms_cuentas.domain.model.TipoMovimiento;

public record RegistrarMovimientoCommand(
        Long cuentaId,
        TipoMovimiento tipo,
        BigDecimal valor,
        LocalDateTime fecha) {

}
