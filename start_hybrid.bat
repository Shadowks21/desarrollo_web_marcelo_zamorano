@echo off
echo 🚀 Iniciando proyecto híbrido Flask + Spring Boot...
echo.

:: Cambiar al directorio del script
cd /d "%~dp0"

echo 📁 Directorio actual: %CD%
echo.

:: Activar entorno virtual si existe
if exist "venvtareas\Scripts\activate.bat" (
    echo 🐍 Activando entorno virtual...
    call venvtareas\Scripts\activate.bat
)

:: Instalar dependencias si es necesario
if exist "requirements.txt" (
    echo 📦 Instalando dependencias de Python...
    pip install -r requirements.txt >nul 2>&1
)

:: Iniciar Flask en background
echo 🐍 Iniciando Flask en puerto 5000...
start /B python app.py

:: Esperar un poco antes de iniciar Spring Boot
timeout /t 3 /nobreak >nul

:: Iniciar Spring Boot
echo ☕ Iniciando Spring Boot en puerto 8080...
cd java\t4

if exist "mvnw.cmd" (
    start /B mvnw.cmd spring-boot:run
) else (
    start /B mvn spring-boot:run
)

echo.
echo ✅ Ambas aplicaciones iniciadas!
echo 🌐 Flask: http://localhost:5000
echo 🌐 Spring Boot: http://localhost:8080
echo 📊 API Java: http://localhost:8080/api
echo.
echo Presiona cualquier tecla para detener ambas aplicaciones...
pause >nul

:: Matar procesos de Java y Python
taskkill /F /IM java.exe >nul 2>&1
taskkill /F /IM python.exe >nul 2>&1

echo 🛑 Aplicaciones detenidas
pause
