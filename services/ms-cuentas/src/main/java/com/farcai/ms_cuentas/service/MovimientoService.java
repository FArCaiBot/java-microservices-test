package com.farcai.ms_cuentas.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farcai.ms_cuentas.dto.request.RegistrarMovimientoRequest;
import com.farcai.ms_cuentas.exception.ClienteInvalidoException;
import com.farcai.ms_cuentas.exception.SaldoNoDisponibleException;
import com.farcai.ms_cuentas.exception.ValidationException;
import com.farcai.ms_cuentas.mapper.MovimientoMapper;
import com.farcai.ms_cuentas.model.ClienteSnapshotEntity;
import com.farcai.ms_cuentas.model.CuentaEntity;
import com.farcai.ms_cuentas.model.MovimientoEntity;
import com.farcai.ms_cuentas.repository.ClienteSnapshotJpaRepository;
import com.farcai.ms_cuentas.repository.CuentaJpaRepository;
import com.farcai.ms_cuentas.repository.MovimientoJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovimientoService {

    private final CuentaJpaRepository cuentaRepo;
    private final MovimientoJpaRepository movimientoRepo;
    private final ClienteSnapshotJpaRepository clienteSnapshotRepo;
    private final MovimientoMapper movimientoMapper;

    @Transactional
    public MovimientoEntity registrar(RegistrarMovimientoRequest req) {
        if (Objects.isNull(req.numeroCuenta()) || req.numeroCuenta().isBlank()) {
            throw new ValidationException("numeroCuenta es requerido");
        }
        if (Objects.isNull(req.valor())) {
            throw new ValidationException("valor es requerido");
        }
        if (req.valor().compareTo(BigDecimal.ZERO) == 0) {
            throw new ValidationException("El valor del movimiento no puede ser cero");
        }

        CuentaEntity cuenta = cuentaRepo.findByNumeroCuentaForUpdate(req.numeroCuenta())
                .orElseThrow(() -> new ValidationException("Cuenta no existe: " + req.numeroCuenta()));

        if (Boolean.FALSE.equals(cuenta.getEstado())) {
            throw new ValidationException("Cuenta inactiva");
        }

        BigDecimal saldoActual = cuenta.getSaldo();
        BigDecimal nuevoSaldo = saldoActual.add(req.valor());
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new SaldoNoDisponibleException("Saldo no disponible para el retiro");
        }

        cuenta.setSaldo(nuevoSaldo);
        cuentaRepo.save(cuenta);

        LocalDateTime fecha = req.fecha() != null ? req.fecha() : LocalDateTime.now();

        return movimientoRepo.save(movimientoMapper.toEntity(req, cuenta, saldoActual, nuevoSaldo, fecha));
    }

    @Transactional(readOnly = true)
    public ReporteResultado generarReporte(Long clienteId, LocalDate desde, LocalDate hasta) {
        if (clienteId == null) {
            throw new ValidationException("clienteId es requerido");
        }
        if (desde == null) {
            throw new ValidationException("desde es requerido");
        }
        if (hasta == null) {
            throw new ValidationException("hasta es requerido");
        }
        if (hasta.isBefore(desde)) {
            throw new ValidationException("hasta no puede ser menor que desde");
        }

        ClienteSnapshotEntity snap = clienteSnapshotRepo.findById(clienteId)
                .orElseThrow(() -> new ClienteInvalidoException("Cliente no existe en snapshot: " + clienteId));

        if (Boolean.FALSE.equals(snap.getEstado())) {
            throw new ClienteInvalidoException("Cliente inactivo: " + clienteId);
        }

        LocalDateTime desdeTs = desde.atStartOfDay();
        LocalDateTime hastaExclusivo = hasta.plusDays(1).atStartOfDay();

        List<CuentaEntity> cuentas = cuentaRepo.findByClienteId(clienteId);
        List<CuentaConMovimientos> detalle = cuentas.stream()
                .map(c -> new CuentaConMovimientos(
                        c,
                        movimientoRepo.findByCuenta_IdAndFechaBetween(c.getId(), desdeTs, hastaExclusivo)))
                .toList();

        return new ReporteResultado(snap, desde, hasta, detalle);
    }

    public record CuentaConMovimientos(CuentaEntity cuenta, List<MovimientoEntity> movimientos) {
    }

    public record ReporteResultado(
            ClienteSnapshotEntity cliente,
            LocalDate desde,
            LocalDate hasta,
            List<CuentaConMovimientos> cuentas) {
    }
}

