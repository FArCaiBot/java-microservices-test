package com.farcai.ms_cuentas.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.farcai.ms_cuentas.model.TipoMovimiento;

public record MovimientoResponse(Long id,
        Long cuentaId,
        LocalDateTime fecha,
        BigDecimal saldoInicial,
        TipoMovimiento tipo,
        BigDecimal valor,
        BigDecimal saldo) {

}

