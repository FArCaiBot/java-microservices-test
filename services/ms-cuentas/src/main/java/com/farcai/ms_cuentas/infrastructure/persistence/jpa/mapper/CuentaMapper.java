package com.farcai.ms_cuentas.infrastructure.persistence.jpa.mapper;

import com.farcai.ms_cuentas.domain.model.Cuenta;
import com.farcai.ms_cuentas.infrastructure.persistence.jpa.entity.CuentaEntity;

public class CuentaMapper {

    public CuentaEntity toEntity(Cuenta d) {
        CuentaEntity e = new CuentaEntity();
        e.setId(d.getId());
        e.setNumeroCuenta(d.getNumeroCuenta());
        e.setTipoCuenta(d.getTipoCuenta());
        e.setSaldo(d.getSaldo());
        e.setEstado(d.getEstado());
        e.setClienteId(d.getClienteId());
        return e;
    }

    public Cuenta toDomain(CuentaEntity e) {
        return Cuenta.builder()
                .id(e.getId())
                .numeroCuenta(e.getNumeroCuenta())
                .tipoCuenta(e.getTipoCuenta())
                .saldo(e.getSaldo())
                .estado(e.getEstado())
                .clienteId(e.getClienteId())
                .build();
    }

}
