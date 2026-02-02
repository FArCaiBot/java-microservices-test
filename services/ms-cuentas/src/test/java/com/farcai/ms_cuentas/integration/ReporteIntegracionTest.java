package com.farcai.ms_cuentas.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.farcai.ms_cuentas.api.dto.request.CrearCuentaRequest;
import com.farcai.ms_cuentas.api.dto.request.RegistrarMovimientoRequest;
import com.farcai.ms_cuentas.api.dto.response.ReporteEstadoCuentaResponse;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReporteIntegracionTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("cuentas_db")
            .withUsername("postgres")
            .withPassword("123456");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", postgres::getJdbcUrl);
        r.add("spring.datasource.username", postgres::getUsername);
        r.add("spring.datasource.password", postgres::getPassword);
        r.add("spring.jpa.hibernate.ddl-auto", () -> "none");
        r.add("spring.flyway.enabled", () -> "true");
    }

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate rest;

    @Autowired
    JdbcTemplate jdbc;

    @BeforeEach
    void seedClienteSnapshot() {
        jdbc.update("""
                  INSERT INTO cliente_snapshot(cliente_id, nombre, identificacion, estado, updated_at)
                  VALUES (1, 'Juan Perez', '1234567890', true, NOW())
                  ON CONFLICT (cliente_id) DO UPDATE SET
                    nombre = EXCLUDED.nombre,
                    identificacion = EXCLUDED.identificacion,
                    estado = EXCLUDED.estado,
                    updated_at = NOW()
                """);
    }

    @Test
    public void flujoCompleto_crearCuenta_movimiento_y_reporte() {

        // 1) Crear cuenta
        var reqCuenta = new CrearCuentaRequest("478758", "Ahorros", new BigDecimal("2000.00"), 1L);
        ResponseEntity<String> crearCuentaResp = rest.postForEntity("http://localhost:" + port + "/cuentas", reqCuenta,
                String.class);
        assertEquals(HttpStatus.CREATED, crearCuentaResp.getStatusCode());

        // 2) Registrar movimiento (depósito)
        var reqMov = new RegistrarMovimientoRequest("478758", new BigDecimal("100.00"), null);
        ResponseEntity<String> movResp = rest.postForEntity("http://localhost:" + port + "/movimientos", reqMov,
                String.class);
        assertEquals(HttpStatus.CREATED, movResp.getStatusCode());

        // 3) Reporte
        String url = "http://localhost:" + port + "/movimientos/reporte?clienteId=1&desde=" + LocalDate.now()
                + "&hasta=" + LocalDate.now();
        ResponseEntity<ReporteEstadoCuentaResponse> reporteResp = rest.getForEntity(url,
                ReporteEstadoCuentaResponse.class);

        assertEquals(HttpStatus.OK, reporteResp.getStatusCode());
        assertNotNull(reporteResp.getBody());
        assertEquals(1L, reporteResp.getBody().cliente().clienteId());
        assertFalse(reporteResp.getBody().cuentas().isEmpty());
    }
}
