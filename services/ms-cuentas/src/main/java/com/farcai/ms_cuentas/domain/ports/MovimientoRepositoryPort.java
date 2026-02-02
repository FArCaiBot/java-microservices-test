package com.farcai.ms_cuentas.domain.ports;

import java.time.LocalDateTime;
import java.util.List;

import com.farcai.ms_cuentas.domain.model.Movimiento;

public interface MovimientoRepositoryPort {

    Movimiento save(Movimiento movimiento);

    List<Movimiento> findByCuentaIdAndFechaBetween(Long cuentaId, LocalDateTime desde, LocalDateTime hasta);

}
