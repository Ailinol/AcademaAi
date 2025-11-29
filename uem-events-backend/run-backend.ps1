# Script para executar o backend Spring Boot da UEM
# Este script configura o ambiente e inicia o servidor

Write-Host "========================================" -ForegroundColor Green
Write-Host "   UEM Events Backend - Iniciando...   " -ForegroundColor Green  
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

# Detectar Java
$javaPath = (Get-Command java -ErrorAction SilentlyContinue).Source
if (-not $javaPath) {
    Write-Host "ERRO: Java nao encontrado!" -ForegroundColor Red
    Write-Host "Por favor, instale Java 17 ou superior" -ForegroundColor Yellow
    exit 1
}

Write-Host "Java encontrado: $javaPath" -ForegroundColor Cyan
java --version
Write-Host ""

# Verificar se o Maven wrapper existe
if (-not (Test-Path ".mvn\wrapper\maven-wrapper.jar")) {
    Write-Host "Maven wrapper nao encontrado. Baixando..." -ForegroundColor Yellow
    New-Item -ItemType Directory -Force -Path ".mvn\wrapper" | Out-Null
    Invoke-WebRequest -Uri "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar" -OutFile ".mvn\wrapper\maven-wrapper.jar"
    Write-Host "Maven wrapper baixado!" -ForegroundColor Green
}

Write-Host "Compilando e executando o projeto..." -ForegroundColor Cyan
Write-Host "Isso pode levar alguns minutos na primeira vez..." -ForegroundColor Yellow
Write-Host ""

# Executar Maven
$env:MAVEN_OPTS = "-Xms256m -Xmx512m"
java -classpath ".mvn\wrapper\maven-wrapper.jar" "-Dmaven.multiModuleProjectDirectory=$PWD" org.apache.maven.wrapper.MavenWrapperMain spring-boot:run

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "Backend executado com sucesso!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
} else {
    Write-Host ""
    Write-Host "ERRO ao executar o backend!" -ForegroundColor Red
    Write-Host "Codigo de saida: $LASTEXITCODE" -ForegroundColor Red
}
