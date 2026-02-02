package com.farcai.ms_cuentas.infrastructure.persistence.jpa.mapper;

import com.farcai.ms_cuentas.domain.model.Movimiento;
import com.farcai.ms_cuentas.infrastructure.persistence.jpa.entity.MovimientoEntity;

public class MovimientoMapper {

    public MovimientoEntity toEntity(Movimiento d) {
        MovimientoEntity e = new MovimientoEntity();
        e.setId(d.getId());
        e.setCuentaId(d.getCuentaId());
        e.setFecha(d.getFecha());
        e.setSaldoInicial(d.getSaldoInicial());
        e.setTipo(d.getTipo());
        e.setValor(d.getValor());
        e.setSaldo(d.getSaldo());
        return e;
    }

    public Movimiento toDomain(MovimientoEntity e) {
        return Movimiento.builder()
                .id(e.getId())
                .cuentaId(e.getCuentaId())
                .fecha(e.getFecha())
                .saldoInicial(e.getSaldoInicial())
                .tipo(e.getTipo())
                .valor(e.getValor())
                .saldo(e.getSaldo())
                .build();
    }

}
