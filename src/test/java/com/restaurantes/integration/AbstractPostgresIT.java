package com.restaurantes.integration;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * Base para los tests de integración contra un PostgreSQL REAL levantado por Testcontainers.
 *
 * Patrón "singleton container": el contenedor se arranca UNA sola vez (bloque static) y se
 * comparte entre TODAS las clases de integración. No se para por clase (lo reapea Ryuk al
 * terminar la JVM). Así se evita el churn de arrancar/parar un contenedor compartido por cada
 * clase —que daba errores al correr varias clases IT juntas— y además es más rápido (un único
 * contenedor para toda la integración).
 *
 * Claves:
 *  - {@code @ServiceConnection}: Spring saca url/usuario/clave/driver del contenedor y tiene
 *    prioridad sobre el datasource H2 del application.properties de test.
 *  - {@code @Testcontainers(disabledWithoutDocker = true)}: si la máquina no tiene Docker, estos
 *    tests se SALTAN (no fallan), así el build sigue verde sin Docker.
 *
 * OJO Testcontainers 2.0: PostgreSQLContainer vive en org.testcontainers.postgresql y NO es
 * genérica (se instancia sin {@code <>}).
 */
@Testcontainers(disabledWithoutDocker = true)
public abstract class AbstractPostgresIT {

    @ServiceConnection
    static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:18");

    static {
        POSTGRES.start();
    }
}
