package com.farcai.ms_clientes.application.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.farcai.ms_clientes.api.error.NotFoundException;
import com.farcai.ms_clientes.application.dto.ClienteCommand;
import com.farcai.ms_clientes.domain.model.Cliente;
import com.farcai.ms_clientes.domain.model.Persona;
import com.farcai.ms_clientes.domain.ports.ClienteRepositoryPort;
import com.farcai.ms_clientes.infrastructure.messaging.dto.ClienteEventMessage;
import com.farcai.ms_clientes.infrastructure.messaging.rabbit.ClienteEventsPublisher;
import com.farcai.ms_clientes.infrastructure.messaging.rabbit.TxAfterCommitPublisher;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ClienteService {

    private final ClienteRepositoryPort clienteRepositoryPort;
    private final ClienteEventsPublisher publisher;
    private final TxAfterCommitPublisher afterCommit;

    public Cliente crear(ClienteCommand cmd) {
        Persona persona = new Persona(
                cmd.nombre(), cmd.genero(), cmd.edad(), cmd.identificacion(), cmd.direccion(), cmd.telefono());
        Cliente cliente = new Cliente().builder().persona(persona).contrasena(cmd.contrasena()).estado(true).build();
        Cliente saved = clienteRepositoryPort.save(cliente);
        ClienteEventMessage evt = toEvent("ClienteCreado", saved);
        afterCommit.runAfterCommit(() -> publisher.publish("clientes.created", evt));
        return saved;
    }

    public List<Cliente> listar() {
        return clienteRepositoryPort.findAll();
    }

    public Cliente actualizar(Long id, ClienteCommand cmd) {
        Cliente actual = obtener(id);
        Persona persona = new Persona(
                cmd.nombre(), cmd.genero(), cmd.edad(), cmd.identificacion(), cmd.direccion(), cmd.telefono());
        Cliente actualizado = new Cliente(actual.getId(), persona, cmd.contrasena(), actual.isEstado());
        Cliente saved = clienteRepositoryPort.save(actualizado);

        ClienteEventMessage evt = toEvent("ClienteActualizado", saved);
        afterCommit.runAfterCommit(() -> publisher.publish("clientes.updated", evt));

        return saved;
    }

    public Cliente desactivar(Long id) {
        Cliente actual = obtener(id);
        actual.setEstado(false);
        Cliente saved = clienteRepositoryPort.save(actual);
        ClienteEventMessage evt = toEvent("ClienteDesactivado", saved);
        afterCommit.runAfterCommit(() -> publisher.publish("clientes.disabled", evt));

        return saved;
    }

    public Cliente obtener(Long id) {
        return clienteRepositoryPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado: " + id));
    }

    private ClienteEventMessage toEvent(String type, Cliente c) {
        return new ClienteEventMessage(
                UUID.randomUUID().toString(),
                type,
                Instant.now(),
                1,
                new ClienteEventMessage.Data(
                        c.getId(),
                        c.getPersona().getNombre(),
                        c.getPersona().getIdentificacion(),
                        c.isEstado()));
    }

}
