# Instalar Docker en Windows

Para esta aplicación **solo necesitas Docker si vas a usar PostgreSQL**: arrancar la app en el perfil
`prod` o ejecutar los tests de integración (`*PostgresTest`, con Testcontainers). Para el desarrollo
normal y para casi todos los tests, que usan H2, no hace falta Docker.

En Windows, Docker se instala con **Docker Desktop**.

## 1. Requisitos

- Windows 10 u 11 (64 bits).
- WSL2 (el instalador de Docker Desktop lo activa por ti si no lo tienes).
- Virtualización activada en la BIOS (en la mayoría de equipos ya viene activada de fábrica).

## 2. Instalar Docker Desktop

1. Descarga Docker Desktop desde https://www.docker.com/products/docker-desktop/
2. Ejecuta el instalador. Deja marcada la opción **"Use WSL 2"**.
3. Reinicia el ordenador si te lo pide.
4. Abre **Docker Desktop**. La primera vez tarda un poco en arrancar.
5. Cuando el icono de la ballena (abajo a la derecha, en la barra de tareas) deje de moverse y esté
   **en verde**, Docker está listo.

## 3. Comprobar que funciona

Abre una terminal (PowerShell, o la terminal integrada de IntelliJ) y ejecuta:

```bash
docker run hello-world
```

Si ves un mensaje que empieza por "Hello from Docker!", todo está correcto.

## 4. Comandos y uso en este proyecto

Para **arrancar la app en perfil `prod`** (PostgreSQL):

```bash
docker compose up -d     # levanta PostgreSQL en segundo plano
docker compose down      # lo para (conserva los datos)
docker compose down -v   # lo para y borra los datos
docker ps                # ver qué contenedores están corriendo
```

Para los **tests de integración** (`*PostgresTest`) **no necesitas ningún comando**: Testcontainers
levanta y destruye su propio PostgreSQL al ejecutar `mvn test`. Solo tienes que tener **Docker Desktop
abierto**. Si no lo tienes abierto, esos tests se saltan y el resto sigue pasando.

## 5. Problemas frecuentes

- **"Cannot connect to the Docker daemon" / "Docker Desktop is starting".**
  Docker Desktop no está abierto o todavía está arrancando. Ábrelo y espera a que la ballena esté en verde.

- **"WSL 2 installation is incomplete" o similar.**
  Abre PowerShell **como administrador** y ejecuta `wsl --install`. Reinicia y vuelve a abrir Docker Desktop.

- **El instalador avisa de la virtualización.**
  Entra en la BIOS/UEFI y activa la virtualización (Intel VT-x / AMD-V, a veces llamada "SVM").
  Suele estar en la sección de CPU o "Advanced".

- **Docker Desktop pide iniciar sesión.**
  No es obligatorio para esta práctica; puedes usarlo sin cuenta.

Con la ballena en verde y `docker run hello-world` funcionando, ya puedes usar PostgreSQL y los tests
de integración.
