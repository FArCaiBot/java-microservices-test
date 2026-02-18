package com.farcai.ms_cuentas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farcai.ms_cuentas.model.ProcessedEventEntity;

public interface ProcessedEventJpaRepository extends JpaRepository<ProcessedEventEntity, String> {
}
