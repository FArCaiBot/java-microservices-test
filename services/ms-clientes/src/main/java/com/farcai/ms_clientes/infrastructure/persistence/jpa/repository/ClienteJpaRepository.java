package com.farcai.ms_clientes.infrastructure.persistence.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farcai.ms_clientes.infrastructure.persistence.jpa.entity.ClienteEntity;

public interface ClienteJpaRepository extends JpaRepository<ClienteEntity, Long> {

    Optional<ClienteEntity> findByPersonaIdentificacion(String identificacion);

}
