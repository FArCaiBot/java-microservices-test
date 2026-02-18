package com.farcai.ms_clientes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farcai.ms_clientes.model.ClienteEntity;

public interface ClienteJpaRepository extends JpaRepository<ClienteEntity, Long> {

    Optional<ClienteEntity> findByPersonaIdentificacion(String identificacion);

}
