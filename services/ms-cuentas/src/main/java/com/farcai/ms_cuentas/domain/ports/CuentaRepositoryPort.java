package com.farcai.ms_cuentas.domain.ports;

import java.util.List;
import java.util.Optional;

import com.farcai.ms_cuentas.domain.model.Cuenta;

public interface CuentaRepositoryPort {

    Cuenta save(Cuenta cuenta);

    Optional<Cuenta> findById(Long id);

    Optional<Cuenta> findByIdForUpdate(Long id);

    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);

    Optional<Cuenta> findByNumeroCuentaForUpdate(String numeroCuenta);

    List<Cuenta> findByClienteId(Long clienteId);

    boolean existsByNumeroCuenta(String numeroCuenta);

}
