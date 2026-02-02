package com.farcai.ms_cuentas.api.dto.response;

public record ClienteReporteResponse(Long clienteId,
        String nombre,
        String identificacion,
        Boolean estado) {

}
