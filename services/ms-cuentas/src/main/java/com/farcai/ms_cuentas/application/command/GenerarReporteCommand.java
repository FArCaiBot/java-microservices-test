package com.farcai.ms_cuentas.application.command;

import java.time.LocalDate;

public record GenerarReporteCommand(Long clienteId,
        LocalDate desde,
        LocalDate hasta) {

}
