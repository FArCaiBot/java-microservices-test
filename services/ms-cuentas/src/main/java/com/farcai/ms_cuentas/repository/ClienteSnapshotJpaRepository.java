package com.farcai.ms_cuentas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farcai.ms_cuentas.model.ClienteSnapshotEntity;

public interface ClienteSnapshotJpaRepository extends JpaRepository<ClienteSnapshotEntity, Long> {
}

