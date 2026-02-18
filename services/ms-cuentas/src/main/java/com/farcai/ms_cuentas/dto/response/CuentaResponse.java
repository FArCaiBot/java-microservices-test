package com.farcai.ms_cuentas.dto.response;

import java.math.BigDecimal;

public record CuentaResponse(Long id,
        String numeroCuenta,
        String tipoCuenta,
        BigDecimal saldo,
        Boolean estado,
        Long clienteId) {

}

