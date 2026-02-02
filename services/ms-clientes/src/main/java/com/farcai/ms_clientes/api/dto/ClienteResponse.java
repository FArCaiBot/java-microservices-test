package com.farcai.ms_clientes.api.dto;

public record ClienteResponse(
        Long id,
        String nombre,
        String identificacion,
        Boolean estado) {

}
