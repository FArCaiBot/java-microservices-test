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
  CONSTRAINT fk_cliente_persona FOREIGN KEY (persona_id) REFERENCES persona(id)
);
