package com.farcai.ms_cuentas.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farcai.ms_cuentas.model.MovimientoEntity;

public interface MovimientoJpaRepository extends JpaRepository<MovimientoEntity, Long> {
    List<MovimientoEntity> findByCuenta_IdAndFechaBetween(Long cuentaId, LocalDateTime desde, LocalDateTime hasta);
}

