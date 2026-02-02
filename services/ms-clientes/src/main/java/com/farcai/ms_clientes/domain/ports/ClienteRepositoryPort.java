package com.farcai.ms_clientes.domain.ports;

import java.util.List;
import java.util.Optional;

import com.farcai.ms_clientes.domain.model.Cliente;

public interface ClienteRepositoryPort {
    Cliente save(Cliente cliente);

    Optional<Cliente> findById(Long id);

    Optional<Cliente> findByIdentificacion(String identificacion);

    List<Cliente> findAll();

}
