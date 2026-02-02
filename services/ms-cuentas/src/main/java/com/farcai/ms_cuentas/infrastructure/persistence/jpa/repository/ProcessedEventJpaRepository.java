package com.farcai.ms_cuentas.infrastructure.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farcai.ms_cuentas.infrastructure.persistence.jpa.entity.ProcessedEventEntity;

public interface ProcessedEventJpaRepository extends JpaRepository<ProcessedEventEntity, String> {
}