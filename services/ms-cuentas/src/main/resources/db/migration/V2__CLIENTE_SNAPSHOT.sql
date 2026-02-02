CREATE TABLE cliente_snapshot (
  cliente_id BIGINT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  identificacion VARCHAR(20) NOT NULL,
  estado BOOLEAN NOT NULL,
  updated_at TIMESTAMP NOT NULL
);

CREATE TABLE processed_event (
  event_id VARCHAR(60) PRIMARY KEY,
  processed_at TIMESTAMP NOT NULL
);
