package com.farcai.ms_clientes.api.controller;

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

import com.farcai.ms_clientes.api.dto.ClienteRequest;
import com.farcai.ms_clientes.api.dto.ClienteResponse;
import com.farcai.ms_clientes.application.dto.ClienteCommand;
import com.farcai.ms_clientes.application.service.ClienteService;
import com.farcai.ms_clientes.domain.model.Cliente;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse crear(@Valid @RequestBody ClienteRequest req) {
        Cliente creado = service.crear(toCommand(req));
        return toResponse(creado);
    }

    @GetMapping("/{id}")
    public ClienteResponse obtener(@PathVariable Long id) {
        return toResponse(service.obtener(id));
    }

    @GetMapping
    public List<ClienteResponse> listar() {
        return service.listar().stream().map(this::toResponse).toList();
    }

    @PutMapping("/{id}")
    public ClienteResponse actualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequest req) {
        return toResponse(service.actualizar(id, toCommand(req)));
    }

    @DeleteMapping("/{id}")
    public ClienteResponse desactivar(@PathVariable Long id) {
        return toResponse(service.desactivar(id));
    }

    private ClienteCommand toCommand(ClienteRequest r) {
        return new ClienteCommand(r.nombre(), r.genero(), r.edad(), r.identificacion(), r.direccion(), r.telefono(),
                r.contrasena());
    }

    private ClienteResponse toResponse(Cliente c) {
        return new ClienteResponse(c.getId(), c.getPersona().getNombre(), c.getPersona().getIdentificacion(),
                c.isEstado());
    }

}
