package com.farcai.ms_clientes.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
                @NotBlank @Size(min = 3, max = 50) String nombre,
                @Size(max = 1) String genero,
                @Min(0) Integer edad,
                @NotBlank @Pattern(regexp = "^[0-9]{10}$", message = "Debe ser de 10 digitos") String identificacion,
                String direccion,
                @Pattern(regexp = "^[0-9]{10}$",message = "Debe ser de 10 digitos") String telefono,
                @NotBlank String contrasena) {

}
