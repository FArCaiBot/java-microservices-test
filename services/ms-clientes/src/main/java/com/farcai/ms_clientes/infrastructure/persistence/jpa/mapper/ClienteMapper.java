package com.farcai.ms_clientes.infrastructure.persistence.jpa.mapper;

import com.farcai.ms_clientes.domain.model.Cliente;
import com.farcai.ms_clientes.domain.model.Persona;
import com.farcai.ms_clientes.infrastructure.persistence.jpa.entity.ClienteEntity;
import com.farcai.ms_clientes.infrastructure.persistence.jpa.entity.PersonaEntity;

public class ClienteMapper {

    public ClienteEntity toEntity(Cliente domain) {
        ClienteEntity e = new ClienteEntity();
        e.setId(domain.getId());
        e.setContrasena(domain.getContrasena());
        e.setEstado(domain.isEstado());

        PersonaEntity p = new PersonaEntity();
        p.setNombre(domain.getPersona().getNombre());
        p.setGenero(domain.getPersona().getGenero());
        p.setEdad(domain.getPersona().getEdad());
        p.setIdentificacion(domain.getPersona().getIdentificacion());
        p.setDireccion(domain.getPersona().getDireccion());
        p.setTelefono(domain.getPersona().getTelefono());

        e.setPersona(p);
        return e;
    }

    public Cliente toDomain(ClienteEntity e) {
        PersonaEntity p = e.getPersona();
        Persona persona = new Persona(
                p.getNombre(), p.getGenero(), p.getEdad(), p.getIdentificacion(), p.getDireccion(), p.getTelefono());
        return new Cliente(e.getId(), persona, e.getContrasena(), Boolean.TRUE.equals(e.getEstado()));
    }

}
