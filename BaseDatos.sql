-- =========================================================
-- BaseDatos.sql
-- Esquema final de la solución
-- Microservicios: ms-clientes / ms-cuentas
-- Base de datos: PostgreSQL
-- =========================================================

-- =========================
-- MS-CLIENTES
-- =========================

CREATE TABLE persona (
  id BIGSERIAL PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  genero VARCHAR(20),
  edad INT,
  identificacion VARCHAR(20) NOT NULL,
  direccion VARCHAR(200),
  telefono VARCHAR(20),
  CONSTRAINT uk_persona_identificacion UNIQUE (identificacion)
);

CREATE TABLE cliente (
  id BIGSERIAL PRIMARY KEY,
  persona_id BIGINT NOT NULL,
  contrasena VARCHAR(120) NOT NULL,
  estado BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT fk_cliente_persona
    FOREIGN KEY (persona_id) REFERENCES persona(id)
);

-- =========================
-- MS-CUENTAS
-- =========================

-- Snapshot de clientes (event-driven)
CREATE TABLE cliente_snapshot (
  cliente_id BIGINT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  identificacion VARCHAR(20) NOT NULL,
  estado BOOLEAN NOT NULL,
  updated_at TIMESTAMP NOT NULL
);

-- Control de idempotencia de eventos
CREATE TABLE processed_event (
  event_id VARCHAR(60) PRIMARY KEY,
  processed_at TIMESTAMP NOT NULL
);

-- Cuentas
CREATE TABLE cuenta (
  id BIGSERIAL PRIMARY KEY,
  numero_cuenta VARCHAR(30) NOT NULL,
  tipo_cuenta VARCHAR(30) NOT NULL,
  saldo NUMERIC(19,2) NOT NULL,
  estado BOOLEAN NOT NULL DEFAULT TRUE,
  cliente_id BIGINT NOT NULL,
  CONSTRAINT uk_cuenta_numero UNIQUE (numero_cuenta)
);

-- Movimientos
CREATE TABLE movimiento (
  id BIGSERIAL PRIMARY KEY,
  cuenta_id BIGINT NOT NULL,
  fecha TIMESTAMP NOT NULL,
  saldo_inicial NUMERIC(19,2) NOT NULL,
  tipo VARCHAR(20) NOT NULL,
  valor NUMERIC(19,2) NOT NULL,
  saldo NUMERIC(19,2) NOT NULL,
  CONSTRAINT fk_mov_cuenta
    FOREIGN KEY (cuenta_id) REFERENCES cuenta(id)
);

-- Índice para reportes por rango de fechas
CREATE INDEX idx_mov_cuenta_fecha
  ON movimiento (cuenta_id, fecha);
