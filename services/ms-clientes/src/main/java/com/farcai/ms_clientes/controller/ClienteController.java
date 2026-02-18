package com.farcai.ms_clientes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.farcai.ms_clientes.dto.ClienteRequest;
import com.farcai.ms_clientes.dto.ClienteResponse;
import com.farcai.ms_clientes.mapper.ClienteMapper;
import com.farcai.ms_clientes.service.ClienteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;
    private final ClienteMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse crear(@Valid @RequestBody ClienteRequest req) {
        return mapper.toResponse(service.crear(req));
    }

    @GetMapping("/{id}")
    public ClienteResponse obtener(@PathVariable Long id) {
        return mapper.toResponse(service.obtener(id));
    }

    @GetMapping
    public List<ClienteResponse> listar() {
        return service.listar().stream().map(mapper::toResponse).toList();
    }

    @PutMapping("/{id}")
    public ClienteResponse actualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequest req) {
        return mapper.toResponse(service.actualizar(id, req));
    }

    @DeleteMapping("/{id}")
    public ClienteResponse desactivar(@PathVariable Long id) {
        return mapper.toResponse(service.desactivar(id));
    }

}
