CREATE TABLE cuenta (
  id BIGSERIAL PRIMARY KEY,
  numero_cuenta VARCHAR(30) NOT NULL,
  tipo_cuenta VARCHAR(30) NOT NULL,
  saldo NUMERIC(19,2) NOT NULL,
  estado BOOLEAN NOT NULL DEFAULT TRUE,
  cliente_id BIGINT NOT NULL,
  CONSTRAINT uk_cuenta_numero UNIQUE (numero_cuenta)
);

CREATE TABLE movimiento (
  id BIGSERIAL PRIMARY KEY,
  cuenta_id BIGINT NOT NULL,
  fecha TIMESTAMP NOT NULL,
  saldo_inicial NUMERIC(19,2) NOT NULL,
  tipo VARCHAR(20) NOT NULL,
  valor NUMERIC(19,2) NOT NULL,
  saldo NUMERIC(19,2) NOT NULL,
  CONSTRAINT fk_mov_cuenta FOREIGN KEY (cuenta_id) REFERENCES cuenta(id)
);

CREATE INDEX idx_mov_cuenta_fecha ON movimiento (cuenta_id, fecha);
