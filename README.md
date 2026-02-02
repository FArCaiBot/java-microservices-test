# senior-backend monorepo

Monorepo con dos microservicios Spring Boot y su infraestructura local
(PostgreSQL + RabbitMQ). Incluye contenedores Docker y el esquema base.

## Servicios

- ms-clientes: gestion de clientes y personas
  - API base: http://localhost:8081
  - Base de datos: postgres-clientes (clientes_db) puerto 5433
- ms-cuentas: cuentas, movimientos y reporte de estado de cuenta
  - API base: http://localhost:8082
  - Base de datos: postgres-cuentas (cuentas_db) puerto 5434
- rabbitmq: broker de eventos
  - AMQP: 5672
  - UI: http://localhost:15672 (guest/guest)

## Requisitos

- Java 21
- Docker Desktop
- Maven (opcional, se incluye mvnw)

## Quick start con Docker

```bash
docker compose up --build
```

Servicios levantados:

- ms-clientes: http://localhost:8081
- ms-cuentas: http://localhost:8082
- RabbitMQ UI: http://localhost:15672

## Ejecutar local sin Docker (por servicio)

Levanta primero RabbitMQ y las bases de datos (puedes usar el
`docker-compose.yml` solo para infraestructura).

```bash
docker compose up rabbitmq postgres-clientes postgres-cuentas
```

ms-clientes:

```bash
cd services/ms-clientes
./mvnw spring-boot:run
```

ms-cuentas:

```bash
cd services/ms-cuentas
./mvnw spring-boot:run
```

Las configuraciones por defecto usan:

- ms-clientes: `jdbc:postgresql://localhost:5433/clientes_db`
- ms-cuentas: `jdbc:postgresql://localhost:5434/cuentas_db`
- usuario/password: `postgres` / `123456`

## Endpoints principales

ms-clientes (`/clientes`)

- POST `/clientes`
- GET `/clientes/{id}`
- GET `/clientes`
- PUT `/clientes/{id}`
- DELETE `/clientes/{id}`

ms-cuentas

- POST `/cuentas`
- POST `/movimientos`
- GET `/movimientos/reporte?clienteId=1&desde=2026-01-01&hasta=2026-01-31`

## Base de datos

El esquema de referencia esta en `BaseDatos.sql`.
Cada microservicio tiene su propia base de datos y Flyway habilitado.

## Estructura del repo

```
/postman
services/
  ms-clientes/
  ms-cuentas/
docker-compose.yml
BaseDatos.sql
```
