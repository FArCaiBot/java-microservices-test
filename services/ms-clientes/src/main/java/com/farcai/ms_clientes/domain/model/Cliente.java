package com.farcai.ms_clientes.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
@Builder
public class Cliente {
    private Long id;
    private Persona persona;
    private String contrasena;
    private boolean estado;

    public Cliente(Long id, Persona persona, String contrasena, boolean estado) {
        if (persona == null)
            throw new IllegalArgumentException("persona es requerida");
        if (contrasena == null || contrasena.isBlank())
            throw new IllegalArgumentException("contrasena es requerida");
        this.id = id;
        this.persona = persona;
        this.contrasena = contrasena;
        this.estado = estado;
    }
}
