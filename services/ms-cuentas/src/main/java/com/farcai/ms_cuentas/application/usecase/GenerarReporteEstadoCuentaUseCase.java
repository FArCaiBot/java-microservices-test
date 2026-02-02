package com.farcai.ms_cuentas.application.usecase;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.farcai.ms_cuentas.application.command.GenerarReporteCommand;
import com.farcai.ms_cuentas.domain.exceptions.ClienteInvalidoException;
import com.farcai.ms_cuentas.domain.exceptions.ValidationException;
import com.farcai.ms_cuentas.domain.model.Cuenta;
import com.farcai.ms_cuentas.domain.model.Movimiento;
import com.farcai.ms_cuentas.domain.ports.ClienteSnapshotRepositoryPort;
import com.farcai.ms_cuentas.domain.ports.CuentaRepositoryPort;
import com.farcai.ms_cuentas.domain.ports.MovimientoRepositoryPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GenerarReporteEstadoCuentaUseCase {

    private final ClienteSnapshotRepositoryPort clienteSnapshotRepo;
    private final CuentaRepositoryPort cuentaRepo;
    private final MovimientoRepositoryPort movimientoRepo;

    @Transactional(readOnly = true)
    public ReporteResultado ejecutar(GenerarReporteCommand cmd) {
        if (cmd.clienteId() == null)
            throw new ValidationException("clienteId es requerido");
        if (cmd.desde() == null)
            throw new ValidationException("desde es requerido");
        if (cmd.hasta() == null)
            throw new ValidationException("hasta es requerido");
        if (cmd.hasta().isBefore(cmd.desde()))
            throw new ValidationException("hasta no puede ser menor que desde");

        var snap = clienteSnapshotRepo.findById(cmd.clienteId())
                .orElseThrow(() -> new ClienteInvalidoException("Cliente no existe en snapshot: " + cmd.clienteId()));

        if (Boolean.FALSE.equals(snap.estado())) {
            throw new ClienteInvalidoException("Cliente inactivo: " + cmd.clienteId());
        }

        LocalDateTime desde = cmd.desde().atStartOfDay();
        // hasta inclusivo -> convertimos a exclusivo (siguiente día a las 00:00)
        LocalDateTime hastaExclusivo = cmd.hasta().plusDays(1).atStartOfDay();

        List<Cuenta> cuentas = cuentaRepo.findByClienteId(cmd.clienteId());

        List<CuentaConMovimientos> detalle = cuentas.stream()
                .map(c -> new CuentaConMovimientos(
                        c,
                        movimientoRepo.findByCuentaIdAndFechaBetween(c.getId(), desde, hastaExclusivo)))
                .toList();

        return new ReporteResultado(
                new ClienteInfo(snap.clienteId(), snap.nombre(), snap.identificacion(), snap.estado()),
                cmd.desde(),
                cmd.hasta(),
                detalle);
    }

    // Modelos internos del resultado (application-level)
    public record ClienteInfo(Long clienteId, String nombre, String identificacion, Boolean estado) {
    }

    public record CuentaConMovimientos(Cuenta cuenta, List<Movimiento> movimientos) {
    }

    public record ReporteResultado(
            ClienteInfo cliente,
            java.time.LocalDate desde,
            java.time.LocalDate hasta,
            List<CuentaConMovimientos> cuentas) {
    }

}
