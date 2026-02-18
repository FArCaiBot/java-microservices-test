package com.farcai.ms_cuentas.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.farcai.ms_cuentas.dto.request.RegistrarMovimientoRequest;
import com.farcai.ms_cuentas.dto.response.MovimientoResponse;
import com.farcai.ms_cuentas.dto.response.ReporteEstadoCuentaResponse;
import com.farcai.ms_cuentas.mapper.MovimientoMapper;
import com.farcai.ms_cuentas.service.MovimientoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/movimientos")
public class MovimientoController {
        private final MovimientoService movimientoService;
        private final MovimientoMapper movimientoMapper;

        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public MovimientoResponse registrar(@Valid @RequestBody RegistrarMovimientoRequest req) {
                return movimientoMapper.toResponse(movimientoService.registrar(req));
        }

        @GetMapping("/reporte")
        public ReporteEstadoCuentaResponse estadoCuenta(@RequestParam Long clienteId,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
                return movimientoMapper.toReporteResponse(movimientoService.generarReporte(clienteId, desde, hasta));
        }
}

