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
        if (cmd.numeroCuenta() == null)
            throw new ValidationException("numeroCuenta es requerido");
        if (cmd.valor() == null)
            throw new ValidationException("valor es requerido");
        
        if(cmd.valor().compareTo(BigDecimal.ZERO) == 0) {
            throw new ValidationException("El valor del movimiento no puede ser cero");
        }

        // lock de la fila para evitar retiros concurrentes con saldo inconsistente
        Cuenta cuenta = cuentaRepo.findByNumeroCuentaForUpdate(cmd.numeroCuenta())
                .orElseThrow(() -> new ValidationException("Cuenta no existe: " + cmd.numeroCuenta()));

        if (Boolean.FALSE.equals(cuenta.getEstado())) {
            throw new ValidationException("Cuenta inactiva");
        }

        BigDecimal saldoActual = cuenta.getSaldo();
        BigDecimal nuevoSaldo;

        nuevoSaldo = saldoActual.add(cmd.valor());
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new SaldoNoDisponibleException("Saldo no disponible para el retiro");
        }

        Cuenta actualizada = cuenta.aplicarNuevoSaldo(nuevoSaldo);
        cuentaRepo.save(actualizada);

        LocalDateTime fecha = (cmd.fecha() != null) ? cmd.fecha() : LocalDateTime.now();

        Movimiento mov = Movimiento.crear(cuenta.getId(), fecha,
                saldoActual,
                cmd.valor().compareTo(BigDecimal.ZERO) < 0 ? TipoMovimiento.RETIRO : TipoMovimiento.DEPOSITO,
                cmd.valor(), nuevoSaldo);
        return movimientoRepo.save(mov);
    }

}
