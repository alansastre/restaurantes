# Autoprovision de SonarQube Community LOCAL (Windows / PowerShell).
# - Quita la contrasena por defecto de 'admin' (evita el aviso "Update your password").
# - Crea un quality gate FLEXIBLE, lo pone por defecto, crea el proyecto y genera el token.
# Idempotente: se puede ejecutar varias veces.
#
#   Ejecutar desde la raiz del repo:  .\sonar\provision.ps1
$ErrorActionPreference = 'Stop'
$Sonar   = 'http://localhost:9000'
$NEWPASS = 'Adecco.Sonar.2026'   # contrasena local de admin (instancia efimera de clase)

function Auth($p) { @{ Authorization = "Basic " + [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("admin:$p")) } }
$headers = Auth 'admin'   # se reasigna tras resolver la contrasena

function Wait-Sonar {
  Write-Host "Esperando a que SonarQube este UP en $Sonar (puede tardar 1-2 min)..."
  for ($i = 0; $i -lt 90; $i++) {
    try {
      $s = Invoke-RestMethod "$Sonar/api/system/status" -TimeoutSec 5
      if ($s.status -eq 'UP') { Write-Host "SonarQube UP."; return }
      Write-Host "  estado: $($s.status) ..."
    } catch { Write-Host "  arrancando ..." }
    Start-Sleep -Seconds 5
  }
  throw "SonarQube no llego a UP a tiempo. Revisa: docker compose -f compose.sonar.yaml logs sonarqube"
}

function Test-Login($p) {
  try { return [bool](Invoke-RestMethod "$Sonar/api/authentication/validate" -Headers (Auth $p) -TimeoutSec 5).valid }
  catch { return $false }
}

# POST tolerante: no aborta si el recurso ya existia (lo registra y sigue).
function Sonar-Post($path, $body) {
  try { return Invoke-RestMethod -Method Post -Uri "$Sonar$path" -Headers $headers -Body $body }
  catch { Write-Host "  (aviso) POST $path -> $($_.Exception.Message)"; return $null }
}

Wait-Sonar

# 0) Resolver credenciales y quitar la contrasena por defecto
if (Test-Login 'admin') {
  # Instancia recien creada: cambiar admin/admin -> NEWPASS (asi la UI no fuerza el cambio)
  try {
    Invoke-RestMethod -Method Post "$Sonar/api/users/change_password" -Headers (Auth 'admin') `
      -Body @{ login = 'admin'; previousPassword = 'admin'; password = $NEWPASS } -ErrorAction Stop | Out-Null
    Write-Host "Contrasena de 'admin' establecida (ya no usa la de por defecto)."
  } catch { Write-Host "  (aviso) no se pudo cambiar la contrasena: $($_.Exception.Message)" }
  $headers = Auth $NEWPASS
} elseif (Test-Login $NEWPASS) {
  $headers = Auth $NEWPASS   # ya estaba provisionada en una ejecucion anterior
} else {
  throw "No puedo autenticar como admin (ni 'admin/admin' ni 'admin/$NEWPASS')."
}

# 1) New Code global = 90 dias (el maximo) -> mas estable, evita 'Not computed'
Sonar-Post '/api/new_code_periods/set' @{ type = 'NUMBER_OF_DAYS'; value = '90' } | Out-Null

# 2) Quality gate flexible
Sonar-Post '/api/qualitygates/create' @{ name = 'Curso Adecco' } | Out-Null
# SonarQube copia las condiciones estrictas de 'Sonar way' (new_coverage 80, new_violations 0,
# hotspots 100%...). Las borramos TODAS para dejar el gate realmente flexible.
try {
  $cur = Invoke-RestMethod "$Sonar/api/qualitygates/show?name=Curso%20Adecco" -Headers $headers
  foreach ($c in $cur.conditions) { Sonar-Post '/api/qualitygates/delete_condition' @{ id = $c.id } | Out-Null }
} catch { Write-Host "  (aviso) no se pudieron limpiar condiciones heredadas: $($_.Exception.Message)" }
# Solo nuestras dos condiciones, sobre codigo OVERALL (siempre se calculan -> verde estable)
Sonar-Post '/api/qualitygates/create_condition' @{ gateName = 'Curso Adecco'; metric = 'coverage';                 op = 'LT'; error = '50' } | Out-Null
Sonar-Post '/api/qualitygates/create_condition' @{ gateName = 'Curso Adecco'; metric = 'duplicated_lines_density'; op = 'GT'; error = '10' } | Out-Null
Sonar-Post '/api/qualitygates/set_as_default' @{ name = 'Curso Adecco' } | Out-Null

# 3) Proyecto local (key independiente de la de SonarCloud)
Sonar-Post '/api/projects/create' @{ project = 'restaurantes-testing'; name = 'restaurantes-testing' } | Out-Null

# 4) Token de analisis (revoca el anterior si existe y regenera)
Sonar-Post '/api/user_tokens/revoke' @{ name = 'local-analysis' } | Out-Null
$tok = Sonar-Post '/api/user_tokens/generate' @{ name = 'local-analysis' }
if (-not $tok -or -not $tok.token) { throw "No se pudo generar el token." }
$tok.token | Set-Content -Path 'sonar\token.txt' -NoNewline -Encoding ascii

Write-Host ""
Write-Host "OK -> login admin / $NEWPASS | gate 'Curso Adecco' por defecto | proyecto 'restaurantes-testing' | token en sonar\token.txt"
Write-Host "Siguiente paso:  .\sonar\analyze.ps1"
