package com.farcai.ms_clientes.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farcai.ms_clientes.dto.ClienteRequest;
import com.farcai.ms_clientes.exception.NotFoundException;
import com.farcai.ms_clientes.mapper.ClienteMapper;
import com.farcai.ms_clientes.messaging.dto.ClienteEventMessage;
import com.farcai.ms_clientes.messaging.rabbit.ClienteEventsPublisher;
import com.farcai.ms_clientes.messaging.rabbit.TxAfterCommitPublisher;
import com.farcai.ms_clientes.model.ClienteEntity;
import com.farcai.ms_clientes.repository.ClienteJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ClienteService {

    private final ClienteJpaRepository clienteJpaRepository;
    private final ClienteMapper mapper;
    private final ClienteEventsPublisher publisher;
    private final TxAfterCommitPublisher afterCommit;

    @Transactional
    public ClienteEntity crear(ClienteRequest req) {
        ClienteEntity cliente = mapper.toNewEntity(req);
        ClienteEntity saved = clienteJpaRepository.save(cliente);
        ClienteEventMessage evt = toEvent("ClienteCreado", saved);
        afterCommit.runAfterCommit(() -> publisher.publish("clientes.created", evt));
        return saved;
    }

    public List<ClienteEntity> listar() {
        return clienteJpaRepository.findAll();
    }

    @Transactional
    public ClienteEntity actualizar(Long id, ClienteRequest req) {
        ClienteEntity actual = obtener(id);
        mapper.updateEntity(actual, req);
        ClienteEntity saved = clienteJpaRepository.save(actual);

        ClienteEventMessage evt = toEvent("ClienteActualizado", saved);
        afterCommit.runAfterCommit(() -> publisher.publish("clientes.updated", evt));

        return saved;
    }

    @Transactional
    public ClienteEntity desactivar(Long id) {
        ClienteEntity actual = obtener(id);
        actual.setEstado(false);
        ClienteEntity saved = clienteJpaRepository.save(actual);
        ClienteEventMessage evt = toEvent("ClienteDesactivado", saved);
        afterCommit.runAfterCommit(() -> publisher.publish("clientes.disabled", evt));

        return saved;
    }

    public ClienteEntity obtener(Long id) {
        return clienteJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado: " + id));
    }

    private ClienteEventMessage toEvent(String type, ClienteEntity c) {
        return mapper.toEventMessage(UUID.randomUUID().toString(), type, Instant.now(), c);
    }

}
