package com.farcai.ms_clientes.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ClienteTest {

    @Test
    void nuevoCliente_debeIniciarActivo() {
        Persona persona = new Persona("Juan Perez", "M", 30, "1234567890", "Av Siempre Viva 123", "0998765432");
        Cliente cliente = new Cliente().builder()
                .persona(persona)
                .contrasena("securePassword")
                .estado(true)
                .build();

        assert (cliente.isEstado());
        assertNull(cliente.getId());
        assertEquals("1234567890", cliente.getPersona().getIdentificacion());
    }

    @Test
    void crearCliente_sinPersona_debeFallar() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Cliente(null, null, "secreta", true));
        assertTrue(ex.getMessage().toLowerCase().contains("persona"));
    }

}
