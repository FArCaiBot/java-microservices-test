package com.farcai.ms_clientes.api.dto;

import jakarta.validation.constraints.NotBlank;

public record ClienteRequest(
        @NotBlank String nombre,
        String genero,
        Integer edad,
        @NotBlank String identificacion,
        String direccion,
        String telefono,
        @NotBlank String contrasena) {

}
