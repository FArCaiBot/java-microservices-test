package com.farcai.ms_cuentas.application.usecase;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.farcai.ms_cuentas.application.command.RegistrarMovimientoCommand;
import com.farcai.ms_cuentas.domain.exceptions.SaldoNoDisponibleException;
import com.farcai.ms_cuentas.domain.exceptions.ValidationException;
import com.farcai.ms_cuentas.domain.model.Cuenta;
import com.farcai.ms_cuentas.domain.model.Movimiento;
import com.farcai.ms_cuentas.domain.model.TipoMovimiento;
import com.farcai.ms_cuentas.domain.ports.CuentaRepositoryPort;
import com.farcai.ms_cuentas.domain.ports.MovimientoRepositoryPort;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RegistrarMovimientoUseCase {
    private final CuentaRepositoryPort cuentaRepo;
    private final MovimientoRepositoryPort movimientoRepo;

    @Transactional
    public Movimiento ejecutar(RegistrarMovimientoCommand cmd) {
        if (cmd.cuentaId() == null)
            throw new ValidationException("cuentaId es requerido");
        if (cmd.tipo() == null)
            throw new ValidationException("tipo es requerido");
        if (cmd.valor() == null)
            throw new ValidationException("valor es requerido");
        if (cmd.valor().compareTo(BigDecimal.ZERO) <= 0)
            throw new ValidationException("valor debe ser > 0");

        // lock de la fila para evitar retiros concurrentes con saldo inconsistente
        Cuenta cuenta = cuentaRepo.findByIdForUpdate(cmd.cuentaId())
                .orElseThrow(() -> new ValidationException("Cuenta no existe: " + cmd.cuentaId()));

        if (Boolean.FALSE.equals(cuenta.getEstado())) {
            throw new ValidationException("Cuenta inactiva");
        }

        BigDecimal saldoActual = cuenta.getSaldo();
        BigDecimal nuevoSaldo;

        if (cmd.tipo() == TipoMovimiento.DEPOSITO) {
            nuevoSaldo = saldoActual.add(cmd.valor());
        } else {
            // RETIRO
            nuevoSaldo = saldoActual.subtract(cmd.valor());
            if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
                throw new SaldoNoDisponibleException("Saldo no disponible");
            }
        }

        Cuenta actualizada = cuenta.aplicarNuevoSaldo(nuevoSaldo);
        cuentaRepo.save(actualizada);

        LocalDateTime fecha = (cmd.fecha() != null) ? cmd.fecha() : LocalDateTime.now();
        BigDecimal valorMovimiento = (cmd.tipo() == TipoMovimiento.RETIRO) ? cmd.valor().negate() : cmd.valor();

        Movimiento mov = Movimiento.crear(cuenta.getId(), fecha, cmd.tipo(), valorMovimiento, nuevoSaldo);
        return movimientoRepo.save(mov);
    }

}
