package com.farcai.ms_cuentas.api.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record CuentaReporteResponse(Long cuentaId,
        String numeroCuenta,
        String tipoCuenta,
        BigDecimal saldoActual,
        Boolean estado,
        List<MovimientoResponse> movimientos) {

}
