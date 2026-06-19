#!/usr/bin/env bash
# Corre tests + cobertura (JaCoCo) y sube el analisis al SonarQube LOCAL.
# NO toca el pom.xml: todo va por -D, asi SonarCloud/CI quedan intactos.
# Ejecutar desde la raiz del repo (tras provision.sh):  ./sonar/analyze.sh
set -euo pipefail
[ -f sonar/token.txt ] || { echo "Falta sonar/token.txt. Ejecuta antes:  ./sonar/provision.sh"; exit 1; }
TOKEN=$(cat sonar/token.txt)

./mvnw -B clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token="$TOKEN" \
  -Dsonar.projectKey=restaurantes-testing \
  -Dsonar.projectName=restaurantes-testing

echo ""
echo "Analisis subido. Abre:  http://localhost:9000/dashboard?id=restaurantes-testing"
