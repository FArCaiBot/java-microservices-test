package com.farcai.ms_cuentas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.farcai.ms_cuentas.model.CuentaEntity;

import jakarta.persistence.LockModeType;

public interface CuentaJpaRepository extends JpaRepository<CuentaEntity, Long> {

    boolean existsByNumeroCuenta(String numeroCuenta);

    List<CuentaEntity> findByClienteId(Long clienteId);

    Optional<CuentaEntity> findByNumeroCuenta(String numeroCuenta);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from CuentaEntity c where c.id = :id")
    Optional<CuentaEntity> findByIdForUpdate(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from CuentaEntity c where c.numeroCuenta = :numero")
    Optional<CuentaEntity> findByNumeroCuentaForUpdate(@Param("numero") String numeroCuenta);

}

