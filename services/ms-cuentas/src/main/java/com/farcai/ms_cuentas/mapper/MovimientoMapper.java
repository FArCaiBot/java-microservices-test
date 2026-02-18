package com.farcai.ms_cuentas.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.farcai.ms_cuentas.dto.request.RegistrarMovimientoRequest;
import com.farcai.ms_cuentas.dto.response.ClienteReporteResponse;
import com.farcai.ms_cuentas.dto.response.CuentaReporteResponse;
import com.farcai.ms_cuentas.dto.response.MovimientoResponse;
import com.farcai.ms_cuentas.dto.response.ReporteEstadoCuentaResponse;
import com.farcai.ms_cuentas.model.CuentaEntity;
import com.farcai.ms_cuentas.model.MovimientoEntity;
import com.farcai.ms_cuentas.model.TipoMovimiento;
import com.farcai.ms_cuentas.service.MovimientoService;

@Component
public class MovimientoMapper {

    public MovimientoEntity toEntity(
            RegistrarMovimientoRequest req,
            CuentaEntity cuenta,
            BigDecimal saldoInicial,
            BigDecimal nuevoSaldo,
            LocalDateTime fecha) {
        MovimientoEntity mov = new MovimientoEntity();
        mov.setCuenta(cuenta);
        mov.setFecha(fecha);
        mov.setSaldoInicial(saldoInicial);
        mov.setTipo(req.valor().compareTo(BigDecimal.ZERO) < 0 ? TipoMovimiento.RETIRO : TipoMovimiento.DEPOSITO);
        mov.setValor(req.valor());
        mov.setSaldo(nuevoSaldo);
        return mov;
    }

    public MovimientoResponse toResponse(MovimientoEntity m) {
        return new MovimientoResponse(m.getId(), m.getCuenta().getId(), m.getFecha(), m.getSaldoInicial(), m.getTipo(),
                m.getValor(), m.getSaldo());
    }

    public ReporteEstadoCuentaResponse toReporteResponse(MovimientoService.ReporteResultado result) {
        return new ReporteEstadoCuentaResponse(
                new ClienteReporteResponse(
                        result.cliente().getClienteId(),
                        result.cliente().getNombre(),
                        result.cliente().getIdentificacion(),
                        result.cliente().getEstado()),
                result.desde(),
                result.hasta(),
                result.cuentas().stream().map(this::toCuentaReporte).toList());
    }

    public CuentaReporteResponse toCuentaReporte(MovimientoService.CuentaConMovimientos cm) {
        CuentaEntity c = cm.cuenta();
        List<MovimientoResponse> movs = cm.movimientos().stream().map(this::toResponse).toList();
        return new CuentaReporteResponse(c.getId(), c.getNumeroCuenta(), c.getTipoCuenta(), c.getSaldo(), c.getEstado(),
                movs);
    }
}
