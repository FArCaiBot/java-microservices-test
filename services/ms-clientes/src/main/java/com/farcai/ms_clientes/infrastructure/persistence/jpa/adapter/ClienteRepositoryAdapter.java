package com.farcai.ms_clientes.infrastructure.persistence.jpa.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.farcai.ms_clientes.domain.model.Cliente;
import com.farcai.ms_clientes.domain.ports.ClienteRepositoryPort;
import com.farcai.ms_clientes.infrastructure.persistence.jpa.mapper.ClienteMapper;
import com.farcai.ms_clientes.infrastructure.persistence.jpa.repository.ClienteJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClienteRepositoryAdapter implements ClienteRepositoryPort {

    private final ClienteJpaRepository clienteJpaRepository;
    private final ClienteMapper mapper = new ClienteMapper();

    @Override
    public Cliente save(Cliente cliente) {
        return mapper.toDomain(clienteJpaRepository.save(mapper.toEntity(cliente)));
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return clienteJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Cliente> findByIdentificacion(String identificacion) {
        return clienteJpaRepository.findByPersonaIdentificacion(identificacion).map(mapper::toDomain);
    }

    @Override
    public List<Cliente> findAll() {
        return clienteJpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

}
