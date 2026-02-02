package com.farcai.ms_cuentas.api.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.farcai.ms_cuentas.api.dto.request.RegistrarMovimientoRequest;
import com.farcai.ms_cuentas.api.dto.response.ClienteReporteResponse;
import com.farcai.ms_cuentas.api.dto.response.CuentaReporteResponse;
import com.farcai.ms_cuentas.api.dto.response.MovimientoResponse;
import com.farcai.ms_cuentas.api.dto.response.ReporteEstadoCuentaResponse;
import com.farcai.ms_cuentas.application.command.GenerarReporteCommand;
import com.farcai.ms_cuentas.application.command.RegistrarMovimientoCommand;
import com.farcai.ms_cuentas.application.usecase.GenerarReporteEstadoCuentaUseCase;
import com.farcai.ms_cuentas.application.usecase.RegistrarMovimientoUseCase;
import com.farcai.ms_cuentas.domain.model.Cuenta;
import com.farcai.ms_cuentas.domain.model.Movimiento;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RestController
@RequestMapping("/movimientos")
public class MovimientoController {
        private final RegistrarMovimientoUseCase registrarMovimiento;
        private final GenerarReporteEstadoCuentaUseCase reporteUseCase;

        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public MovimientoResponse registrar(@Valid @RequestBody RegistrarMovimientoRequest req) {
                Movimiento m = registrarMovimiento.ejecutar(new RegistrarMovimientoCommand(
                                req.numeroCuenta(), req.valor(), req.fecha()));
                return toResponse(m);
        }

        @GetMapping("/reporte")
        public ReporteEstadoCuentaResponse estadoCuenta(@RequestParam Long clienteId,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
                var result = reporteUseCase.ejecutar(new GenerarReporteCommand(clienteId, desde, hasta));

                return new ReporteEstadoCuentaResponse(
                                new ClienteReporteResponse(
                                                result.cliente().clienteId(),
                                                result.cliente().nombre(),
                                                result.cliente().identificacion(),
                                                result.cliente().estado()),
                                result.desde(),
                                result.hasta(),
                                result.cuentas().stream().map(this::toCuentaReporte).toList());
        }

        private MovimientoResponse toResponse(Movimiento m) {
                return new MovimientoResponse(m.getId(), m.getCuentaId(), m.getFecha(), m.getSaldoInicial(), m.getTipo(), m.getValor(),
                                m.getSaldo());
        }

        private CuentaReporteResponse toCuentaReporte(GenerarReporteEstadoCuentaUseCase.CuentaConMovimientos cm) {
                Cuenta c = cm.cuenta();
                List<MovimientoResponse> movs = cm.movimientos().stream().map(this::toMovimientoResponse).toList();

                return new CuentaReporteResponse(
                                c.getId(),
                                c.getNumeroCuenta(),
                                c.getTipoCuenta(),
                                c.getSaldo(),
                                c.getEstado(),
                                movs);
        }

        private MovimientoResponse toMovimientoResponse(Movimiento m) {
                return new MovimientoResponse(
                                m.getId(),
                                m.getCuentaId(),
                                m.getFecha(),
                                m.getSaldoInicial(),
                                m.getTipo(),
                                m.getValor(),
                                m.getSaldo());
        }
}
