package com.farcai.ms_cuentas.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farcai.ms_cuentas.dto.request.CrearCuentaRequest;
import com.farcai.ms_cuentas.exception.ClienteInvalidoException;
import com.farcai.ms_cuentas.exception.ValidationException;
import com.farcai.ms_cuentas.mapper.CuentaMapper;
import com.farcai.ms_cuentas.model.CuentaEntity;
import com.farcai.ms_cuentas.repository.ClienteSnapshotJpaRepository;
import com.farcai.ms_cuentas.repository.CuentaJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CuentaService {

    private final CuentaJpaRepository cuentaRepo;
    private final ClienteSnapshotJpaRepository clienteSnapshotRepo;
    private final CuentaMapper cuentaMapper;

    @Transactional
    public CuentaEntity crear(CrearCuentaRequest req) {
        var snap = clienteSnapshotRepo.findById(req.clienteId())
                .orElseThrow(() -> new ClienteInvalidoException("Cliente no existe en snapshot: " + req.clienteId()));

        if (Boolean.FALSE.equals(snap.getEstado())) {
            throw new ClienteInvalidoException("Cliente inactivo: " + req.clienteId());
        }

        if (cuentaRepo.existsByNumeroCuenta(req.numeroCuenta())) {
            throw new ValidationException("numeroCuenta ya existe");
        }

        return cuentaRepo.save(cuentaMapper.toEntity(req));
    }
}

