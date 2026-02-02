package com.farcai.ms_clientes.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.farcai.ms_clientes.api.error.NotFoundException;
import com.farcai.ms_clientes.application.dto.ClienteCommand;
import com.farcai.ms_clientes.domain.model.Cliente;
import com.farcai.ms_clientes.domain.model.Persona;
import com.farcai.ms_clientes.domain.ports.ClienteRepositoryPort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ClienteService {

    private final ClienteRepositoryPort clienteRepositoryPort;

    public Cliente crear(ClienteCommand cmd) {
        Persona persona = new Persona(
                cmd.nombre(), cmd.genero(), cmd.edad(), cmd.identificacion(), cmd.direccion(), cmd.telefono());
        Cliente cliente = new Cliente().builder().persona(persona).contrasena(cmd.contrasena()).estado(true).build();
        return clienteRepositoryPort.save(cliente);
    }

    public List<Cliente> listar() {
        return clienteRepositoryPort.findAll();
    }

    public Cliente actualizar(Long id, ClienteCommand cmd) {
        Cliente actual = obtener(id);
        Persona persona = new Persona(
                cmd.nombre(), cmd.genero(), cmd.edad(), cmd.identificacion(), cmd.direccion(), cmd.telefono());
        Cliente actualizado = new Cliente(actual.getId(), persona, cmd.contrasena(), actual.isEstado());
        return clienteRepositoryPort.save(actualizado);
    }

    public Cliente desactivar(Long id) {
        Cliente actual = obtener(id);
        actual.setEstado(false);
        return clienteRepositoryPort.save(actual);
    }

    public Cliente obtener(Long id) {
        return clienteRepositoryPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado: " + id));
    }

}
