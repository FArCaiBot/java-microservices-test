package com.farcai.ms_cuentas.infrastructure.persistence.jpa.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farcai.ms_cuentas.infrastructure.persistence.jpa.entity.MovimientoEntity;

public interface MovimientoJpaRepository extends JpaRepository<MovimientoEntity, Long> {
    List<MovimientoEntity> findByCuentaIdAndFechaBetween(Long cuentaId, LocalDateTime desde, LocalDateTime hasta);
}