# SonarQube local (cobertura + quality gate)

Levanta un **SonarQube Community** en tu máquina para ver la cobertura y el *quality gate*
del proyecto, en `http://localhost:9000`.

## Requisitos
- **Docker Desktop arrancado** (con ~3–4 GB de RAM disponibles; SonarQube usa Elasticsearch).
- **Chrome** instalado (los tests Selenium corren headless durante el análisis).
- Puerto **9000** libre.

## Uso en 3 pasos (Windows / PowerShell)
Desde la raíz del repo (`restaurantes-testing`):
```powershell
docker compose -f compose.sonar.yaml up -d   # 1) levanta SonarQube (1ª vez tarda en bajar imágenes)
.\sonar\provision.ps1                         # 2) crea el quality gate flexible + token (UNA vez)
.\sonar\analyze.ps1                           # 3) corre tests+cobertura y sube el análisis
```
Abre **http://localhost:9000** (usuario `admin`, contraseña `Adecco.Sonar.2026`) →
panel del proyecto: http://localhost:9000/dashboard?id=restaurantes-testing

> `provision` cambia automáticamente la contraseña por defecto de `admin`, así que
> SonarQube **no** te obliga a cambiarla al entrar. (La corta `admin` no vale: SonarQube
> exige ~12+ caracteres con mayúscula, dígito y símbolo.)

### Linux / macOS / WSL / Git Bash
```bash
docker compose -f compose.sonar.yaml up -d
./sonar/provision.sh
./sonar/analyze.sh
```

## El quality gate "Curso Adecco" (flexible)
Se crea y se pone **por defecto** automáticamente. Condiciones sobre código **overall**
(se calculan siempre → verde estable, sin "Not computed"):
- **Coverage** < **50%** → falla (o sea: verde si ≥ 50%)
- **Duplicated Lines (%)** > **10%** → falla

Y *New Code* global a **90 días**. Para cambiar umbrales: edítalos en
`sonar/provision.*` o desde la UI (Quality Gates → Curso Adecco).

## Parar / reiniciar
```powershell
docker compose -f compose.sonar.yaml down      # para (conserva datos en los volúmenes)
docker compose -f compose.sonar.yaml down -v   # para y BORRA todo (empezar de cero)
```

## Problemas frecuentes
- **SonarQube se reinicia solo / no llega a UP:** casi siempre es `vm.max_map_count`
  (lo exige Elasticsearch). En Docker Desktop suele estar bien; si no:
  ```powershell
  wsl -d docker-desktop sysctl -w vm.max_map_count=262144
  ```
  y vuelve a `up -d`. (Para que sea permanente, ponlo en `~/.wslconfig`.)
- **Tarda en arrancar:** la primera vez baja imágenes y levanta Elasticsearch; el script
  `provision` ya espera hasta 7–8 min. Mira logs: `docker compose -f compose.sonar.yaml logs -f sonarqube`.
- **Puerto 9000 ocupado:** cambia `"9000:9000"` por `"9001:9000"` en `compose.sonar.yaml`
  (y usa `-Dsonar.host.url=http://localhost:9001`).

> Es una instancia **local y efímera para clase**: `admin / Adecco.Sonar.2026` está bien aquí.
> No la expongas a internet.
