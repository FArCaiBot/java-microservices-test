package com.farcai.ms_cuentas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
class MovimientoServiceTest {

    @Mock
    private CuentaJpaRepository cuentaRepo;
    @Mock
    private MovimientoJpaRepository movimientoRepo;
    @Mock
    private ClienteSnapshotJpaRepository clienteSnapshotRepo;
    @Mock
    private MovimientoMapper movimientoMapper;

    @Test
    void registrar_debeGuardarMovimientoCuandoDatosSonValidos() {
        MovimientoService service = new MovimientoService(cuentaRepo, movimientoRepo, clienteSnapshotRepo, movimientoMapper);
        LocalDateTime fecha = LocalDateTime.of(2026, 2, 18, 10, 30, 0);
        RegistrarMovimientoRequest req = new RegistrarMovimientoRequest("478758", new BigDecimal("120.50"), fecha);

        CuentaEntity cuenta = cuenta(1L, "478758", new BigDecimal("1000.00"), true);
        MovimientoEntity entity = movimiento(cuenta, fecha, new BigDecimal("1000.00"), new BigDecimal("120.50"),
                new BigDecimal("1120.50"));
        MovimientoEntity saved = movimiento(cuenta, fecha, new BigDecimal("1000.00"), new BigDecimal("120.50"),
                new BigDecimal("1120.50"));
        saved.setId(10L);

        when(cuentaRepo.findByNumeroCuentaForUpdate("478758")).thenReturn(Optional.of(cuenta));
        when(movimientoMapper.toEntity(req, cuenta, new BigDecimal("1000.00"), new BigDecimal("1120.50"), fecha))
                .thenReturn(entity);
        when(movimientoRepo.save(entity)).thenReturn(saved);

        MovimientoEntity result = service.registrar(req);

        assertSame(saved, result);
        assertEquals(new BigDecimal("1120.50"), cuenta.getSaldo());
        verify(cuentaRepo).save(cuenta);
        verify(movimientoMapper).toEntity(req, cuenta, new BigDecimal("1000.00"), new BigDecimal("1120.50"), fecha);
        verify(movimientoRepo).save(entity);
    }

    @Test
    void registrar_debeLanzarValidationCuandoNumeroCuentaEsNulo() {
        MovimientoService service = new MovimientoService(cuentaRepo, movimientoRepo, clienteSnapshotRepo, movimientoMapper);
        RegistrarMovimientoRequest req = new RegistrarMovimientoRequest(null, new BigDecimal("10.00"), LocalDateTime.now());

        assertThrows(ValidationException.class, () -> service.registrar(req));
    }

    @Test
    void registrar_debeLanzarValidationCuandoValorEsNulo() {
        MovimientoService service = new MovimientoService(cuentaRepo, movimientoRepo, clienteSnapshotRepo, movimientoMapper);
        RegistrarMovimientoRequest req = new RegistrarMovimientoRequest("478758", null, LocalDateTime.now());

        assertThrows(ValidationException.class, () -> service.registrar(req));
    }

    @Test
    void registrar_debeLanzarValidationCuandoValorEsCero() {
        MovimientoService service = new MovimientoService(cuentaRepo, movimientoRepo, clienteSnapshotRepo, movimientoMapper);
        RegistrarMovimientoRequest req = new RegistrarMovimientoRequest("478758", BigDecimal.ZERO, LocalDateTime.now());

        assertThrows(ValidationException.class, () -> service.registrar(req));
    }

    @Test
    void registrar_debeLanzarValidationCuandoCuentaNoExiste() {
        MovimientoService service = new MovimientoService(cuentaRepo, movimientoRepo, clienteSnapshotRepo, movimientoMapper);
        RegistrarMovimientoRequest req = new RegistrarMovimientoRequest("478758", new BigDecimal("10.00"), LocalDateTime.now());
        when(cuentaRepo.findByNumeroCuentaForUpdate("478758")).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> service.registrar(req));
    }

    @Test
    void registrar_debeLanzarValidationCuandoCuentaEstaInactiva() {
        MovimientoService service = new MovimientoService(cuentaRepo, movimientoRepo, clienteSnapshotRepo, movimientoMapper);
        RegistrarMovimientoRequest req = new RegistrarMovimientoRequest("478758", new BigDecimal("10.00"), LocalDateTime.now());
        when(cuentaRepo.findByNumeroCuentaForUpdate("478758"))
                .thenReturn(Optional.of(cuenta(1L, "478758", new BigDecimal("1000.00"), false)));

        assertThrows(ValidationException.class, () -> service.registrar(req));
    }

    @Test
    void registrar_debeLanzarSaldoNoDisponibleCuandoRetiroExcedeSaldo() {
        MovimientoService service = new MovimientoService(cuentaRepo, movimientoRepo, clienteSnapshotRepo, movimientoMapper);
        RegistrarMovimientoRequest req = new RegistrarMovimientoRequest("478758", new BigDecimal("-1200.00"), LocalDateTime.now());
        when(cuentaRepo.findByNumeroCuentaForUpdate("478758"))
                .thenReturn(Optional.of(cuenta(1L, "478758", new BigDecimal("1000.00"), true)));

        assertThrows(SaldoNoDisponibleException.class, () -> service.registrar(req));
    }

    @Test
    void generarReporte_debeRetornarResultadoConCuentasYMovimientos() {
        MovimientoService service = new MovimientoService(cuentaRepo, movimientoRepo, clienteSnapshotRepo, movimientoMapper);
        Long clienteId = 1L;
        LocalDate desde = LocalDate.of(2026, 2, 1);
        LocalDate hasta = LocalDate.of(2026, 2, 18);

        ClienteSnapshotEntity snap = clienteSnapshot(clienteId, true);
        CuentaEntity cuenta = cuenta(7L, "478758", new BigDecimal("1000.00"), true);
        List<MovimientoEntity> movs = List.of(
                movimiento(cuenta, LocalDateTime.of(2026, 2, 10, 9, 0), new BigDecimal("1000.00"),
                        new BigDecimal("100.00"), new BigDecimal("1100.00")));

        when(clienteSnapshotRepo.findById(clienteId)).thenReturn(Optional.of(snap));
        when(cuentaRepo.findByClienteId(clienteId)).thenReturn(List.of(cuenta));
        when(movimientoRepo.findByCuenta_IdAndFechaBetween(
                eq(7L), eq(LocalDateTime.of(2026, 2, 1, 0, 0)), eq(LocalDateTime.of(2026, 2, 19, 0, 0))))
                        .thenReturn(movs);

        MovimientoService.ReporteResultado result = service.generarReporte(clienteId, desde, hasta);

        assertSame(snap, result.cliente());
        assertEquals(desde, result.desde());
        assertEquals(hasta, result.hasta());
        assertEquals(1, result.cuentas().size());
        assertSame(cuenta, result.cuentas().get(0).cuenta());
        assertEquals(1, result.cuentas().get(0).movimientos().size());
        assertSame(movs.get(0), result.cuentas().get(0).movimientos().get(0));
    }

    @Test
    void generarReporte_debeLanzarValidationCuandoClienteIdEsNulo() {
        MovimientoService service = new MovimientoService(cuentaRepo, movimientoRepo, clienteSnapshotRepo, movimientoMapper);

        assertThrows(ValidationException.class, () -> service.generarReporte(null, LocalDate.now(), LocalDate.now()));
    }

    @Test
    void generarReporte_debeLanzarValidationCuandoDesdeEsNulo() {
        MovimientoService service = new MovimientoService(cuentaRepo, movimientoRepo, clienteSnapshotRepo, movimientoMapper);

        assertThrows(ValidationException.class, () -> service.generarReporte(1L, null, LocalDate.now()));
    }

    @Test
    void generarReporte_debeLanzarValidationCuandoHastaEsNulo() {
        MovimientoService service = new MovimientoService(cuentaRepo, movimientoRepo, clienteSnapshotRepo, movimientoMapper);

        assertThrows(ValidationException.class, () -> service.generarReporte(1L, LocalDate.now(), null));
    }

    @Test
    void generarReporte_debeLanzarValidationCuandoHastaEsMenorQueDesde() {
        MovimientoService service = new MovimientoService(cuentaRepo, movimientoRepo, clienteSnapshotRepo, movimientoMapper);

        assertThrows(ValidationException.class,
                () -> service.generarReporte(1L, LocalDate.of(2026, 2, 18), LocalDate.of(2026, 2, 1)));
    }

    @Test
    void generarReporte_debeLanzarClienteInvalidoCuandoSnapshotNoExiste() {
        MovimientoService service = new MovimientoService(cuentaRepo, movimientoRepo, clienteSnapshotRepo, movimientoMapper);
        when(clienteSnapshotRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ClienteInvalidoException.class,
                () -> service.generarReporte(1L, LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 18)));
    }

    @Test
    void generarReporte_debeLanzarClienteInvalidoCuandoClienteEstaInactivo() {
        MovimientoService service = new MovimientoService(cuentaRepo, movimientoRepo, clienteSnapshotRepo, movimientoMapper);
        when(clienteSnapshotRepo.findById(1L)).thenReturn(Optional.of(clienteSnapshot(1L, false)));

        assertThrows(ClienteInvalidoException.class,
                () -> service.generarReporte(1L, LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 18)));
    }

    private static CuentaEntity cuenta(Long id, String numero, BigDecimal saldo, boolean estado) {
        CuentaEntity c = new CuentaEntity();
        c.setId(id);
        c.setNumeroCuenta(numero);
        c.setTipoCuenta("AHORRO");
        c.setSaldo(saldo);
        c.setEstado(estado);
        c.setClienteId(1L);
        return c;
    }

    private static MovimientoEntity movimiento(CuentaEntity cuenta, LocalDateTime fecha, BigDecimal saldoInicial,
            BigDecimal valor, BigDecimal saldoFinal) {
        MovimientoEntity m = new MovimientoEntity();
        m.setCuenta(cuenta);
        m.setFecha(fecha);
        m.setSaldoInicial(saldoInicial);
        m.setValor(valor);
        m.setSaldo(saldoFinal);
        return m;
    }

    private static ClienteSnapshotEntity clienteSnapshot(Long id, boolean estado) {
        ClienteSnapshotEntity snap = new ClienteSnapshotEntity();
        snap.setClienteId(id);
        snap.setNombre("Jose");
        snap.setIdentificacion("1718137159");
        snap.setEstado(estado);
        snap.setUpdatedAt(LocalDateTime.now());
        assertNotNull(snap.getUpdatedAt());
        return snap;
    }
}
