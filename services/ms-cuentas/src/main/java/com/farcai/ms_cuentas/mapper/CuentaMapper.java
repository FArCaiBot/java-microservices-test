package com.farcai.ms_cuentas.mapper;

import org.springframework.stereotype.Component;

import com.farcai.ms_cuentas.dto.request.CrearCuentaRequest;
import com.farcai.ms_cuentas.dto.response.CuentaResponse;
import com.farcai.ms_cuentas.model.CuentaEntity;

@Component
public class CuentaMapper {

    public CuentaEntity toEntity(CrearCuentaRequest req) {
        CuentaEntity cuenta = new CuentaEntity();
        cuenta.setNumeroCuenta(req.numeroCuenta());
        cuenta.setTipoCuenta(req.tipoCuenta());
        cuenta.setSaldo(req.saldoInicial());
        cuenta.setEstado(true);
        cuenta.setClienteId(req.clienteId());
        return cuenta;
    }

    public CuentaResponse toResponse(CuentaEntity c) {
        return new CuentaResponse(c.getId(), c.getNumeroCuenta(), c.getTipoCuenta(), c.getSaldo(), c.getEstado(),
                c.getClienteId());
    }
}
