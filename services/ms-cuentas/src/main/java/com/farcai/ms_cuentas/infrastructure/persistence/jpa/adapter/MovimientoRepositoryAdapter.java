package com.farcai.ms_cuentas.infrastructure.persistence.jpa.adapter;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.farcai.ms_cuentas.domain.model.Movimiento;
import com.farcai.ms_cuentas.domain.ports.MovimientoRepositoryPort;
import com.farcai.ms_cuentas.infrastructure.persistence.jpa.mapper.MovimientoMapper;
import com.farcai.ms_cuentas.infrastructure.persistence.jpa.repository.MovimientoJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MovimientoRepositoryAdapter implements MovimientoRepositoryPort {

    private final MovimientoJpaRepository jpa;
    private final MovimientoMapper mapper = new MovimientoMapper();

    @Override
    public Movimiento save(Movimiento movimiento) {
        return mapper.toDomain(jpa.save(mapper.toEntity(movimiento)));
    }

    @Override
    public List<Movimiento> findByCuentaIdAndFechaBetween(Long cuentaId, LocalDateTime desde, LocalDateTime hasta) {
        return jpa.findByCuentaIdAndFechaBetween(cuentaId, desde, hasta).stream().map(mapper::toDomain).toList();
    }
}
