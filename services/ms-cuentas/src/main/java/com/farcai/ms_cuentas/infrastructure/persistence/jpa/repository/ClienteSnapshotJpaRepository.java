package com.farcai.ms_cuentas.infrastructure.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farcai.ms_cuentas.infrastructure.persistence.jpa.entity.ClienteSnapshotEntity;

public interface ClienteSnapshotJpaRepository extends JpaRepository<ClienteSnapshotEntity, Long> {
}
