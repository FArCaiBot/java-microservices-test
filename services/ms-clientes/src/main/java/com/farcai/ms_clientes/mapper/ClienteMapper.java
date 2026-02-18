package com.farcai.ms_clientes.mapper;

import java.time.Instant;

import org.springframework.stereotype.Component;

import com.farcai.ms_clientes.dto.ClienteRequest;
import com.farcai.ms_clientes.dto.ClienteResponse;
import com.farcai.ms_clientes.messaging.dto.ClienteEventMessage;
import com.farcai.ms_clientes.model.ClienteEntity;
import com.farcai.ms_clientes.model.PersonaEntity;

@Component
public class ClienteMapper {

    public ClienteEntity toNewEntity(ClienteRequest req) {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setPersona(toPersona(req));
        cliente.setContrasena(req.contrasena());
        cliente.setEstado(true);
        return cliente;
    }

    public void updateEntity(ClienteEntity cliente, ClienteRequest req) {
        PersonaEntity persona = cliente.getPersona() == null ? new PersonaEntity() : cliente.getPersona();
        persona.setNombre(req.nombre());
        persona.setGenero(req.genero());
        persona.setEdad(req.edad());
        persona.setIdentificacion(req.identificacion());
        persona.setDireccion(req.direccion());
        persona.setTelefono(req.telefono());

        cliente.setPersona(persona);
        cliente.setContrasena(req.contrasena());
    }

    public ClienteResponse toResponse(ClienteEntity c) {
        return new ClienteResponse(
                c.getId(),
                c.getPersona() != null ? c.getPersona().getNombre() : null,
                c.getPersona() != null ? c.getPersona().getIdentificacion() : null,
                c.getEstado());
    }

    public ClienteEventMessage toEventMessage(String eventId, String eventType, Instant occurredAt, ClienteEntity c) {
        return new ClienteEventMessage(
                eventId,
                eventType,
                occurredAt,
                1,
                new ClienteEventMessage.Data(
                        c.getId(),
                        c.getPersona() != null ? c.getPersona().getNombre() : null,
                        c.getPersona() != null ? c.getPersona().getIdentificacion() : null,
                        c.getEstado()));
    }

    private PersonaEntity toPersona(ClienteRequest req) {
        PersonaEntity persona = new PersonaEntity();
        persona.setNombre(req.nombre());
        persona.setGenero(req.genero());
        persona.setEdad(req.edad());
        persona.setIdentificacion(req.identificacion());
        persona.setDireccion(req.direccion());
        persona.setTelefono(req.telefono());
        return persona;
    }
}
