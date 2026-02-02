package com.farcai.ms_cuentas.domain.model;

import java.math.BigDecimal;

import com.farcai.ms_cuentas.domain.exceptions.ValidationException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Cuenta {
    private final Long id;
    private final String numeroCuenta;
    private final String tipoCuenta;
    private final BigDecimal saldo;
    private final Boolean estado;
    private final Long clienteId;

    public static Cuenta nueva(String numeroCuenta, String tipoCuenta, BigDecimal saldoInicial, Long clienteId) {
        if (numeroCuenta == null || numeroCuenta.isBlank())
            throw new ValidationException("numeroCuenta es requerido");
        if (tipoCuenta == null || tipoCuenta.isBlank())
            throw new ValidationException("tipoCuenta es requerido");
        if (saldoInicial == null)
            throw new ValidationException("saldoInicial es requerido");
        if (clienteId == null)
            throw new ValidationException("clienteId es requerido");
        if (saldoInicial.compareTo(BigDecimal.ZERO) < 0)
            throw new ValidationException("saldoInicial no puede ser negativo");

        return Cuenta.builder()
                .id(null)
                .numeroCuenta(numeroCuenta)
                .tipoCuenta(tipoCuenta)
                .saldo(saldoInicial)
                .estado(true)
                .clienteId(clienteId)
                .build();
    }

    public Cuenta aplicarNuevoSaldo(BigDecimal nuevoSaldo) {
        if (nuevoSaldo == null)
            throw new ValidationException("nuevoSaldo es requerido");
        return this.toBuilder().saldo(nuevoSaldo).build();
    }

    public Cuenta desactivar() {
        return this.toBuilder().estado(false).build();
    }

}
