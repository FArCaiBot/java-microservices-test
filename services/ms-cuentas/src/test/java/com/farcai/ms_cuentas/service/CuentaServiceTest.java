package com.farcai.ms_cuentas.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.farcai.ms_cuentas.dto.request.CrearCuentaRequest;
import com.farcai.ms_cuentas.exception.ClienteInvalidoException;
import com.farcai.ms_cuentas.exception.ValidationException;
import com.farcai.ms_cuentas.mapper.CuentaMapper;
import com.farcai.ms_cuentas.model.ClienteSnapshotEntity;
import com.farcai.ms_cuentas.model.CuentaEntity;
import com.farcai.ms_cuentas.repository.ClienteSnapshotJpaRepository;
import com.farcai.ms_cuentas.repository.CuentaJpaRepository;

@ExtendWith(MockitoExtension.class)
class CuentaServiceTest {

    @Mock
    private CuentaJpaRepository cuentaRepo;
    @Mock
    private ClienteSnapshotJpaRepository clienteSnapshotRepo;
    @Mock
    private CuentaMapper cuentaMapper;

    @Test
    void crear_debeGuardarCuentaCuandoClienteActivoYNumeroDisponible() {
        CuentaService service = new CuentaService(cuentaRepo, clienteSnapshotRepo, cuentaMapper);
        CrearCuentaRequest req = request();
        ClienteSnapshotEntity snap = clienteSnapshot(1L, true);
        CuentaEntity entity = cuenta(1L, "478758", new BigDecimal("2000.00"));
        CuentaEntity saved = cuenta(10L, "478758", new BigDecimal("2000.00"));

        when(clienteSnapshotRepo.findById(req.clienteId())).thenReturn(Optional.of(snap));
        when(cuentaRepo.existsByNumeroCuenta(req.numeroCuenta())).thenReturn(false);
        when(cuentaMapper.toEntity(req)).thenReturn(entity);
        when(cuentaRepo.save(entity)).thenReturn(saved);

        CuentaEntity result = service.crear(req);

        assertSame(saved, result);
        verify(clienteSnapshotRepo).findById(req.clienteId());
        verify(cuentaRepo).existsByNumeroCuenta(req.numeroCuenta());
        verify(cuentaMapper).toEntity(req);
        verify(cuentaRepo).save(entity);
    }

    @Test
    void crear_debeLanzarClienteInvalidoCuandoClienteNoExiste() {
        CuentaService service = new CuentaService(cuentaRepo, clienteSnapshotRepo, cuentaMapper);
        CrearCuentaRequest req = request();
        when(clienteSnapshotRepo.findById(req.clienteId())).thenReturn(Optional.empty());

        assertThrows(ClienteInvalidoException.class, () -> service.crear(req));
    }

    @Test
    void crear_debeLanzarClienteInvalidoCuandoClienteEstaInactivo() {
        CuentaService service = new CuentaService(cuentaRepo, clienteSnapshotRepo, cuentaMapper);
        CrearCuentaRequest req = request();
        when(clienteSnapshotRepo.findById(req.clienteId())).thenReturn(Optional.of(clienteSnapshot(1L, false)));

        assertThrows(ClienteInvalidoException.class, () -> service.crear(req));
    }

    @Test
    void crear_debeLanzarValidationCuandoNumeroCuentaYaExiste() {
        CuentaService service = new CuentaService(cuentaRepo, clienteSnapshotRepo, cuentaMapper);
        CrearCuentaRequest req = request();
        when(clienteSnapshotRepo.findById(req.clienteId())).thenReturn(Optional.of(clienteSnapshot(1L, true)));
        when(cuentaRepo.existsByNumeroCuenta(req.numeroCuenta())).thenReturn(true);

        assertThrows(ValidationException.class, () -> service.crear(req));
    }

    private static CrearCuentaRequest request() {
        return new CrearCuentaRequest("478758", "AHORRO", new BigDecimal("2000.00"), 1L);
    }

    private static ClienteSnapshotEntity clienteSnapshot(Long id, boolean estado) {
        return new ClienteSnapshotEntity(id, "Jose", "1718137159", estado, LocalDateTime.now());
    }

    private static CuentaEntity cuenta(Long id, String numero, BigDecimal saldo) {
        CuentaEntity cuenta = new CuentaEntity();
        cuenta.setId(id);
        cuenta.setNumeroCuenta(numero);
        cuenta.setTipoCuenta("AHORRO");
        cuenta.setSaldo(saldo);
        cuenta.setEstado(true);
        cuenta.setClienteId(1L);
        return cuenta;
    }
}
