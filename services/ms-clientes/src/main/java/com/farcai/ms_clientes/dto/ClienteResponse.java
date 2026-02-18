package com.farcai.ms_clientes.dto;

public record ClienteResponse(
        Long id,
        String nombre,
        String identificacion,
        Boolean estado) {

}
