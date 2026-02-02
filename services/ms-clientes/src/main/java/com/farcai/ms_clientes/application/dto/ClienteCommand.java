package com.farcai.ms_clientes.application.dto;

public record ClienteCommand(
        String nombre,
        String genero,
        Integer edad,
        String identificacion,
        String direccion,
        String telefono,
        String contrasena) {
}
