# Corre tests + cobertura (JaCoCo) y sube el analisis al SonarQube LOCAL (Windows / PowerShell).
# NO toca el pom.xml: todo va por -D en linea de comandos, asi SonarCloud/CI quedan intactos.
#
#   Ejecutar desde la raiz del repo (tras provision.ps1):  .\sonar\analyze.ps1
$ErrorActionPreference = 'Stop'
if (-not (Test-Path 'sonar\token.txt')) { throw "Falta sonar\token.txt. Ejecuta antes:  .\sonar\provision.ps1" }
$token = (Get-Content 'sonar\token.txt' -Raw).Trim()

.\mvnw.cmd -B clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar `
  "-Dsonar.host.url=http://localhost:9000" `
  "-Dsonar.token=$token" `
  "-Dsonar.projectKey=restaurantes-testing" `
  "-Dsonar.projectName=restaurantes-testing"

Write-Host ""
Write-Host "Analisis subido. Abre el panel:  http://localhost:9000/dashboard?id=restaurantes-testing"
