package com.farcai.ms_cuentas.application.usecase;

import org.springframework.stereotype.Component;

import com.farcai.ms_cuentas.application.command.CrearCuentaCommand;
import com.farcai.ms_cuentas.domain.exceptions.ValidationException;
import com.farcai.ms_cuentas.domain.model.Cuenta;
import com.farcai.ms_cuentas.domain.ports.CuentaRepositoryPort;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CrearCuentaUseCase {
    private final CuentaRepositoryPort cuentaRepo;

    @Transactional
    public Cuenta ejecutar(CrearCuentaCommand cmd) {
        if (cuentaRepo.existsByNumeroCuenta(cmd.numeroCuenta())) {
            throw new ValidationException("numeroCuenta ya existe");
        }
        Cuenta cuenta = Cuenta.nueva(cmd.numeroCuenta(), cmd.tipoCuenta(), cmd.saldoInicial(), cmd.clienteId());
        return cuentaRepo.save(cuenta);
    }

}
