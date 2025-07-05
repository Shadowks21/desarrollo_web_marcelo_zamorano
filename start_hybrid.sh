#!/bin/bash

# Script para ejecutar Flask y Spring Boot simultáneamente

echo "🚀 Iniciando proyecto híbrido Flask + Spring Boot..."

# Cambiar al directorio del proyecto
cd "$(dirname "$0")"

echo "📁 Directorio actual: $(pwd)"

# Función para limpiar procesos al salir
cleanup() {
    echo "🛑 Deteniendo aplicaciones..."
    if [ ! -z "$FLASK_PID" ]; then
        kill $FLASK_PID 2>/dev/null
    fi
    if [ ! -z "$SPRING_PID" ]; then
        kill $SPRING_PID 2>/dev/null
    fi
    exit 0
}

# Configurar trap para cleanup
trap cleanup SIGINT SIGTERM

# Iniciar Flask en background
echo "🐍 Iniciando Flask en puerto 5000..."
cd "$(dirname "$0")"
python -m venv venvtareas 2>/dev/null || true
source venvtareas/bin/activate 2>/dev/null || source venvtareas/Scripts/activate
pip install -r requirements.txt > /dev/null 2>&1
python app.py &
FLASK_PID=$!

# Esperar un poco antes de iniciar Spring Boot
sleep 3

# Iniciar Spring Boot en background
echo "☕ Iniciando Spring Boot en puerto 8080..."
cd java/t4
./mvnw spring-boot:run &
SPRING_PID=$!

echo "✅ Ambas aplicaciones iniciadas!"
echo "🌐 Flask: http://localhost:5000"
echo "🌐 Spring Boot: http://localhost:8080"
echo "📊 API Java: http://localhost:8080/api"
echo ""
echo "Presiona Ctrl+C para detener ambas aplicaciones"

# Esperar a que terminen los procesos
wait $FLASK_PID $SPRING_PID
