package com.farcai.ms_cuentas.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.farcai.ms_cuentas.api.dto.request.CrearCuentaRequest;
import com.farcai.ms_cuentas.api.dto.response.CuentaResponse;
import com.farcai.ms_cuentas.application.command.CrearCuentaCommand;
import com.farcai.ms_cuentas.application.usecase.CrearCuentaUseCase;
import com.farcai.ms_cuentas.domain.model.Cuenta;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    private final CrearCuentaUseCase crearCuenta;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CuentaResponse crear(@Valid @RequestBody CrearCuentaRequest req) {
        Cuenta c = crearCuenta.ejecutar(new CrearCuentaCommand(
                req.numeroCuenta(), req.tipoCuenta(), req.saldoInicial(), req.clienteId()));
        return toResponse(c);
    }

    private CuentaResponse toResponse(Cuenta c) {
        return new CuentaResponse(c.getId(), c.getNumeroCuenta(), c.getTipoCuenta(), c.getSaldo(), c.getEstado(),
                c.getClienteId());
    }

}
