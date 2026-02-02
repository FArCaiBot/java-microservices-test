package com.farcai.ms_cuentas.infrastructure.persistence.jpa.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.farcai.ms_cuentas.domain.model.Cuenta;
import com.farcai.ms_cuentas.domain.ports.CuentaRepositoryPort;
import com.farcai.ms_cuentas.infrastructure.persistence.jpa.mapper.CuentaMapper;
import com.farcai.ms_cuentas.infrastructure.persistence.jpa.repository.CuentaJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CuentaRepositoryAdapter implements CuentaRepositoryPort {

    private final CuentaJpaRepository jpa;
    private final CuentaMapper mapper = new CuentaMapper();

    @Override
    public Cuenta save(Cuenta cuenta) {
        return mapper.toDomain(jpa.save(mapper.toEntity(cuenta)));
    }

    @Override
    public Optional<Cuenta> findById(Long id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Cuenta> findByIdForUpdate(Long id) {
        return jpa.findByIdForUpdate(id).map(mapper::toDomain);
    }

    @Override
    public List<Cuenta> findByClienteId(Long clienteId) {
        return jpa.findByClienteId(clienteId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existsByNumeroCuenta(String numeroCuenta) {
        return jpa.existsByNumeroCuenta(numeroCuenta);
    }

    @Override
    public Optional<Cuenta> findByNumeroCuenta(String numeroCuenta) {
        return jpa.findByNumeroCuenta(numeroCuenta).map(mapper::toDomain);
    }

    @Override
    public Optional<Cuenta> findByNumeroCuentaForUpdate(String numeroCuenta) {
        return jpa.findByNumeroCuentaForUpdate(numeroCuenta).map(mapper::toDomain);

    }

}
