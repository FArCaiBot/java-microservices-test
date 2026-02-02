package com.farcai.ms_clientes.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class Persona {
    private final String nombre;
    private final String genero;
    private final Integer edad;
    private final String identificacion;
    private final String direccion;
    private final String telefono;
}
