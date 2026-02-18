package com.farcai.ms_cuentas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.farcai.ms_cuentas.dto.request.CrearCuentaRequest;
import com.farcai.ms_cuentas.dto.response.CuentaResponse;
import com.farcai.ms_cuentas.mapper.CuentaMapper;
import com.farcai.ms_cuentas.service.CuentaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;
    private final CuentaMapper cuentaMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CuentaResponse crear(@Valid @RequestBody CrearCuentaRequest req) {
        return cuentaMapper.toResponse(cuentaService.crear(req));
    }

}

