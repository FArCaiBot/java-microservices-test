package com.farcai.ms_cuentas.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.farcai.ms_cuentas.api.dto.request.RegistrarMovimientoRequest;
import com.farcai.ms_cuentas.api.dto.response.MovimientoResponse;
import com.farcai.ms_cuentas.application.command.RegistrarMovimientoCommand;
import com.farcai.ms_cuentas.application.usecase.RegistrarMovimientoUseCase;
import com.farcai.ms_cuentas.domain.model.Movimiento;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/movimientos")
public class MovimientoController {
    private final RegistrarMovimientoUseCase registrarMovimiento;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovimientoResponse registrar(@Valid @RequestBody RegistrarMovimientoRequest req) {
        Movimiento m = registrarMovimiento.ejecutar(new RegistrarMovimientoCommand(
                req.cuentaId(), req.tipo(), req.valor(), req.fecha()));
        return toResponse(m);
    }

    private MovimientoResponse toResponse(Movimiento m) {
        return new MovimientoResponse(m.getId(), m.getCuentaId(), m.getFecha(), m.getTipo(), m.getValor(),
                m.getSaldo());
    }
}
