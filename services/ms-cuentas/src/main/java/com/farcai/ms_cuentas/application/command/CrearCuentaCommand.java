package com.farcai.ms_cuentas.application.command;

import java.math.BigDecimal;

public record CrearCuentaCommand(
        String numeroCuenta,
        String tipoCuenta,
        BigDecimal saldoInicial,
        Long clienteId) {
}