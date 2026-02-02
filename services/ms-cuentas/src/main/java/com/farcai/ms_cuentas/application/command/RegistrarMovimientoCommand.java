package com.farcai.ms_cuentas.application.command;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RegistrarMovimientoCommand(
                String numeroCuenta,
                BigDecimal valor,
                LocalDateTime fecha) {

}
