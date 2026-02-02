package com.farcai.ms_cuentas.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.farcai.ms_cuentas.domain.exceptions.ValidationException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Movimiento {
    private final Long id;
    private final Long cuentaId;
    private final LocalDateTime fecha;
    private final BigDecimal saldoInicial;
    private final TipoMovimiento tipo;
    private final BigDecimal valor;
    private final BigDecimal saldo;

    public static Movimiento crear(Long cuentaId, LocalDateTime fecha, BigDecimal saldoInicial, TipoMovimiento tipo,
            BigDecimal valor,
            BigDecimal saldoResultante) {
        if (cuentaId == null)
            throw new ValidationException("cuentaId es requerido");
        if (fecha == null)
            throw new ValidationException("fecha es requerida");
        if (saldoInicial == null)
            throw new ValidationException("saldoInicial es requerido");
        if (tipo == null)
            throw new ValidationException("tipo es requerido");
        if (valor == null)
            throw new ValidationException("valor es requerido");
        if (valor.compareTo(BigDecimal.ZERO) == 0)
            throw new ValidationException("valor no puede ser 0");
        if (saldoResultante == null)
            throw new ValidationException("saldo resultante es requerido");

        return Movimiento.builder()
                .id(null)
                .cuentaId(cuentaId)
                .fecha(fecha)
                .saldoInicial(saldoInicial)
                .tipo(tipo)
                .valor(valor)
                .saldo(saldoResultante)
                .build();
    }
}
