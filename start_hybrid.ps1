# Script PowerShell para ejecutar Flask y Spring Boot simultáneamente

Write-Host "🚀 Iniciando proyecto híbrido Flask + Spring Boot..." -ForegroundColor Green

# Cambiar al directorio del script
$scriptPath = $MyInvocation.MyCommand.Path
$scriptDir = Split-Path $scriptPath -Parent
Set-Location $scriptDir

Write-Host "📁 Directorio actual: $(Get-Location)" -ForegroundColor Yellow

# Función para limpiar procesos al salir
function Cleanup {
    Write-Host "🛑 Deteniendo aplicaciones..." -ForegroundColor Red
    if ($global:FlaskProcess) {
        Stop-Process -Id $global:FlaskProcess.Id -Force -ErrorAction SilentlyContinue
    }
    if ($global:SpringProcess) {
        Stop-Process -Id $global:SpringProcess.Id -Force -ErrorAction SilentlyContinue
    }
    exit 0
}

# Configurar trap para cleanup
Register-EngineEvent -SourceIdentifier PowerShell.Exiting -Action { Cleanup }

try {
    # Iniciar Flask
    Write-Host "🐍 Iniciando Flask en puerto 5000..." -ForegroundColor Blue
    
    # Activar entorno virtual si existe
    if (Test-Path "venvtareas\Scripts\Activate.ps1") {
        & "venvtareas\Scripts\Activate.ps1"
    }
    
    # Instalar dependencias si es necesario
    if (Test-Path "requirements.txt") {
        Write-Host "📦 Instalando dependencias de Python..." -ForegroundColor Yellow
        pip install -r requirements.txt | Out-Null
    }
    
    # Iniciar Flask en background
    $global:FlaskProcess = Start-Process -FilePath "python" -ArgumentList "app.py" -NoNewWindow -PassThru
    
    Start-Sleep -Seconds 3
    
    # Iniciar Spring Boot
    Write-Host "☕ Iniciando Spring Boot en puerto 8080..." -ForegroundColor Blue
    Set-Location "java\t4"
    
    # Verificar si mvnw existe, si no usar maven directamente
    if (Test-Path "mvnw.cmd") {
        $global:SpringProcess = Start-Process -FilePath "cmd" -ArgumentList "/c", "mvnw.cmd", "spring-boot:run" -NoNewWindow -PassThru
    } else {
        $global:SpringProcess = Start-Process -FilePath "mvn" -ArgumentList "spring-boot:run" -NoNewWindow -PassThru
    }
    
    Write-Host "✅ Ambas aplicaciones iniciadas!" -ForegroundColor Green
    Write-Host "🌐 Flask: http://localhost:5000" -ForegroundColor Cyan
    Write-Host "🌐 Spring Boot: http://localhost:8080" -ForegroundColor Cyan
    Write-Host "📊 API Java: http://localhost:8080/api" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Presiona Ctrl+C para detener ambas aplicaciones" -ForegroundColor Yellow
    
    # Esperar a que terminen los procesos
    Write-Host "⏳ Esperando procesos..." -ForegroundColor Gray
    
    # Bucle para mantener el script activo
    while ($true) {
        Start-Sleep -Seconds 1
        
        # Verificar si los procesos siguen activos
        if ($global:FlaskProcess.HasExited -and $global:SpringProcess.HasExited) {
            Write-Host "❌ Ambas aplicaciones han terminado" -ForegroundColor Red
            break
        }
    }
    
} catch {
    Write-Host "❌ Error: $($_.Exception.Message)" -ForegroundColor Red
} finally {
    Cleanup
}
