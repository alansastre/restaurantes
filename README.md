# Restaurantes (testing)

> Misma aplicación web de restaurantes (catálogo, reseñas, favoritos y pedidos, con Spring Boot
> y Thymeleaf), pero centrada en la **automatización de tests**: tests unitarios y de slice,
> MockMvc, Selenium E2E y tests de integración contra **PostgreSQL real** con Testcontainers.

<p align="center">
  <img src="https://img.shields.io/badge/Java-25-orange?logo=openjdk&logoColor=white" alt="Java 25">
  <img src="https://img.shields.io/badge/Spring%20Boot-4.0.5-6DB33F?logo=springboot&logoColor=white" alt="Spring Boot 4">
  <img src="https://img.shields.io/badge/Tests-JUnit%205-25A162?logo=junit5&logoColor=white" alt="JUnit 5">
  <img src="https://img.shields.io/badge/E2E-Selenium-43B02A?logo=selenium&logoColor=white" alt="Selenium">
  <img src="https://img.shields.io/badge/Integration-Testcontainers-2496ED?logo=docker&logoColor=white" alt="Testcontainers">
  <img src="https://img.shields.io/badge/DB-H2%20%2F%20PostgreSQL-1F6FEB" alt="H2 / PostgreSQL">
</p>

---

## ¿Qué es?

Es la **misma aplicación** que `restaurantes-java` (una web completa renderizada en servidor, no una
API JSON), usada en el grupo de testing para practicar pruebas automatizadas:

- **Cualquier visitante** explora el catálogo de restaurantes y platos, filtra y lee reseñas.
- **Usuarios registrados** escriben reseñas, marcan favoritos y crean pedidos paso a paso.
- **Administradores** gestionan restaurantes, platos, reseñas y usuarios desde el panel.

Sobre esa base, el proyecto tiene una **suite de tests amplia** (repositorios, controladores,
seguridad, API REST, Selenium E2E) y un conjunto de **tests de integración con PostgreSQL real**.

---

## Funcionalidades principales

### Catálogo y descubrimiento
- Listado de restaurantes con **filtros combinables** por tipo de cocina, precio medio y nombre.
- Ficha de cada restaurante con su **carta de platos** y sus **reseñas**.
- Solo se muestran restaurantes **activos** (borrado lógico).

### Reseñas
- Reseñas con **título, descripción y puntuación de 1 a 5**, validadas en el servidor.

### Pedidos
- Pedido asociado a un restaurante con **líneas de pedido**, total y estado.

### Cuentas, seguridad y administración
- **Registro e inicio de sesión** con Spring Security (BCrypt) y control de acceso por roles.
- Panel de administración para gestionar restaurantes, platos, reseñas y usuarios.

---

## Modelo de datos

| Entidad | Descripción |
|--------|-------------|
| **Restaurant** | Restaurante: nombre (único), precio medio, tipo de cocina, estado activo, imagen. |
| **Dish** | Plato: nombre, descripción, precio, tipo (entrante/principal/postre), restaurante. |
| **Review** | Reseña con puntuación 1-5 sobre un restaurante o un plato. |
| **Order** / **OrderLine** | Pedido y sus líneas (plato + cantidad), con total y estado. |
| **User** | Usuario con rol (`ROLE_USER` / `ROLE_ADMIN`), implementa `UserDetails`. |
| **Employee** | Empleado asociado a un restaurante. |

> Nota: las tablas de `User` y `Order` se llaman `users` y `orders` (`@Table`), porque `user` y
> `order` son palabras reservadas en SQL. En H2 a veces se perdonan; en PostgreSQL real no.

---

## Stack tecnológico

- **Java 25** + **Spring Boot 4.0.5**
- **Spring MVC** + **Thymeleaf**, **Spring Data JPA**, **Spring Security**
- Bases de datos: **H2** en memoria y **PostgreSQL 18** (vía Docker)
- **Testing**:
  - **JUnit 5** como motor de tests.
  - **MockMvc** para tests de controladores.
  - **Selenium** para tests E2E de la interfaz.
  - **Testcontainers** (módulo PostgreSQL) para tests de integración contra una BD real.
  - **JaCoCo** para cobertura y **SonarCloud** para análisis.

---

## Bases de datos: qué se usa en cada escenario

Esta es la parte importante, y conviene tenerla muy clara: **qué base de datos usa cada cosa NO
depende solo del perfil de Spring**. Los tests de integración con Testcontainers usan PostgreSQL
**siempre**, sin importar el perfil, porque Testcontainers levanta su propio PostgreSQL por su cuenta.

| Escenario | Cómo se lanza | Base de datos | ¿Necesita Docker? | ¿Comando extra? |
|---|---|---|---|---|
| **Arrancar la app en desarrollo** | Run en IntelliJ, o `mvn spring-boot:run` | **H2** en memoria (perfil `dev`) | No | No |
| **Arrancar la app como producción** | perfil `prod` | **PostgreSQL 18** | Sí | **Sí**: `docker compose up -d` ANTES |
| **Tests normales** (repositorios, MockMvc, seguridad, Selenium...) | `mvn test` | **H2** en memoria (perfil `test`) | No | No |
| **Tests de integración** (`*PostgresTest`) | `mvn test` | **PostgreSQL real y efímero** (lo crea Testcontainers) | Sí (Docker arrancado) | No |

Explicado escenario por escenario:

- **App en desarrollo (H2).** Lo normal del día a día: arrancas y ya. Datos en memoria, se reinician
  en cada arranque. No necesitas Docker ni ningún comando.
- **App como producción (PostgreSQL).** Solo si quieres ver la app contra una BD real. Tienes que
  **levantar PostgreSQL tú** con `docker compose up -d` y luego arrancar con el perfil `prod` (la app
  **no** levanta la BD sola). Ver la sección "Arrancar la aplicación".
- **Tests normales (H2).** Al hacer `mvn test` (o ejecutar los tests en IntelliJ), la gran mayoría
  corren en **H2 en memoria**: rápidos, **sin Docker** y **sin que toques nada**. La configuración de
  H2 para los tests está en `src/test/resources/application.properties` y **no se modifica**.
- **Tests de integración con Testcontainers (PostgreSQL).** Unos pocos tests (los que terminan en
  `PostgresTest`, en el paquete `com.restaurantes.integration`) **no usan H2**: levantan un PostgreSQL
  real dentro de un contenedor Docker, lo usan y lo destruyen al terminar. Esto ocurre **al ejecutar
  `mvn test` igual que el resto** — no hay que lanzar ningún comando especial ni `docker compose`:
  Testcontainers gestiona el contenedor por su cuenta. La única condición es tener **Docker arrancado**.
  Si NO tienes Docker arrancado, esos tests se **saltan** (no fallan), así que `mvn test` sigue en verde.

En una frase: **H2** para arrancar la app en local y para casi todos los tests; **PostgreSQL** solo
cuando arrancas en perfil `prod` (la levantas tú) o cuando corres los tests `*PostgresTest`
(Testcontainers la levanta por ti, sin comando extra).

---

## Arrancar la aplicación

> Requisitos: **JDK 25** (incluye el *wrapper* de Maven). Para PostgreSQL necesitas **Docker**;
> si no lo tienes instalado, mira [DOCKER.md](DOCKER.md).

### Opción A: H2 en memoria (por defecto, sin Docker)

```bash
./mvnw spring-boot:run        # Linux / macOS
mvnw.cmd spring-boot:run      # Windows
```

App en http://localhost:8080 · consola H2 en http://localhost:8080/h2-console
(JDBC URL `jdbc:h2:mem:restaurantes_db`, usuario `sa`, contraseña vacía).

### Opción B: PostgreSQL (perfil `prod`, requiere Docker)

> Hay que arrancar PostgreSQL A MANO antes que la app; la aplicación no levanta la BD sola.

```bash
# 1) Arranca PostgreSQL con Docker Compose (desde la carpeta del proyecto, donde está compose.yaml)
docker compose up -d

# 2) Arranca la app con el perfil prod
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
#    PowerShell: entrecomilla ->  ./mvnw spring-boot:run "-Dspring-boot.run.profiles=prod"
#    IntelliJ:   Edit Configurations > Modify options > Active profiles > prod

# 3) Para la base de datos al terminar
docker compose down       # conserva los datos
docker compose down -v    # borra los datos
```

Conexión (en `compose.yaml`): base `restaurantes`, usuario `restaurantes`, contraseña
`restaurantes`, puerto `5432`. El seeding de demo es **idempotente**: solo siembra si la BD está
vacía, así que puedes reiniciar en `prod` sin duplicar datos.

> Nota: el puerto 5432 lo usa también el `compose.yaml` de `restaurantes-java`. Si tienes ese
> levantado, páralo antes (`docker compose down`) para evitar un conflicto de puerto.

---

## Ejecutar los tests

No hace falta tocar nada en `src/test/resources`.

```bash
mvn test                          # toda la suite
mvn test -Dtest=*RepositoryTest   # solo repositorios (H2)
mvn test -Dtest=*ControllerTest   # solo controladores (MockMvc, H2)
mvn test -Dtest=*SeleniumTest     # solo Selenium E2E (H2)
mvn test -Dtest=*PostgresTest     # solo integración con PostgreSQL (Testcontainers, requiere Docker)
```

En IntelliJ: clic derecho sobre `src/test/java` > Run tests.

Pirámide de tests del proyecto:

| Capa | Herramienta | Base de datos | Docker |
|------|-------------|---------------|:------:|
| Repositorios / slices | `@DataJpaTest` | H2 | No |
| Controladores / API | `@SpringBootTest` + MockMvc | H2 | No |
| Seguridad | MockMvc + Spring Security Test | H2 | No |
| Integración | Testcontainers (`*PostgresTest`) | **PostgreSQL real** | Sí (si no, se saltan) |
| Extremo a extremo | Selenium | H2 | No |

Los tests de integración están en `com.restaurantes.integration` y heredan de `AbstractPostgresIT`
(`@Testcontainers(disabledWithoutDocker = true)` + `@ServiceConnection`), de modo que se saltan
limpiamente si no hay Docker y el build sigue verde.

---

## Integración continua (GitHub Actions)

Workflows en `.github/workflows` (todos manuales, `workflow_dispatch`):

- `tests.yml`: tests por categoría en H2 (`*RepositoryTest`, `*ControllerTest`, `*ServiceTest`,
  `*SecurityTest`). No ejecuta los `*PostgresTest`.
- `selenium.yml`: tests de UI (`*SeleniumTest`) con Chrome.
- `sonar.yml`: `mvn verify` (suite completa + cobertura + SonarCloud). Aquí SÍ entran los tests de
  Testcontainers; los runners `ubuntu-latest` llevan Docker preinstalado, así que se ejecutan sin
  configuración extra (y si no hubiera Docker, se saltarían sin romper el build).

No hay que declarar ningún servicio `postgres` en los workflows: Testcontainers levanta su propio
contenedor.

---

## Cuentas de demo

| Usuario | Contraseña | Rol |
|---------|-----------|-----|
| `admin` | `admin`   | Administrador |
| `user`  | `user`    | Usuario estándar |

---

## Estructura del proyecto

```
src/main/java/com/restaurantes
├── config/        # Seguridad, recursos web y datos de demo (DataInitializer)
├── controller/    # Controladores MVC + API REST
├── model/         # Entidades JPA y enums
├── repository/    # Repositorios Spring Data JPA
└── service/       # Lógica de negocio

src/test/java/com/restaurantes
├── repository/    # Tests de repositorios (@DataJpaTest, H2)
├── controller/    # Tests de controladores y API (MockMvc, H2)
├── security/      # Tests de seguridad
├── ui/            # Tests Selenium E2E
└── integration/   # Tests de integración con PostgreSQL (Testcontainers)

src/main/resources
├── application.properties        # común + perfil por defecto (dev)
├── application-dev.properties    # H2 en memoria
└── application-prod.properties   # PostgreSQL
src/test/resources
└── application.properties        # H2 para los tests (NO se toca)
```

---

<p align="center">
  Hecho con Java y Spring Boot
</p>
