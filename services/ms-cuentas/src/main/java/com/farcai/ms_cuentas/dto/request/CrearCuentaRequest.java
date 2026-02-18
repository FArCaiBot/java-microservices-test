package com.farcai.ms_cuentas.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CrearCuentaRequest(
        @NotBlank @Size(max = 10) String numeroCuenta,
        @NotBlank String tipoCuenta,
        @NotNull @Positive BigDecimal saldoInicial,
        @NotNull Long clienteId) {
}
