package com.farcai.ms_cuentas.api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.farcai.ms_cuentas.domain.model.TipoMovimiento;

public record MovimientoResponse(Long id,
        Long cuentaId,
        LocalDateTime fecha,
        BigDecimal saldoInicial,
        TipoMovimiento tipo,
        BigDecimal valor,
        BigDecimal saldo) {

}
