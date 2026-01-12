---
name: t4-test-e2e
description: Ejecutar tests E2E con Playwright MCP
user_invocable: true
---

# Tests End-to-End con Playwright

Ejecuta pruebas automatizadas de la aplicacion usando Playwright MCP.

## Pre-requisitos
- Aplicacion Spring Boot corriendo en http://localhost:8080
- MCP Playwright configurado

## Casos de Prueba para Tarea 4

### 1. Navegacion a pagina de evaluacion
- Abrir http://localhost:8080/evaluacion
- Verificar que la pagina carga correctamente
- Verificar titulo de la pagina

### 2. Verificar tabla de actividades
- Confirmar que existe tabla con actividades
- Verificar columnas: ID, Fecha Inicio, Sector, Nombre, Tema, nota, evaluar
- Verificar que solo muestra actividades con fecha_termino < hoy

### 3. Flujo de evaluacion
- Click en link "evaluar" de una actividad
- Verificar que aparece selector de nota (1-7)
- Seleccionar una nota
- Verificar que se envia POST a /api/actividades/{id}/nota
- Verificar que el promedio se actualiza sin recargar pagina

### 4. Validacion de notas
- Intentar enviar nota menor a 1 (debe fallar)
- Intentar enviar nota mayor a 7 (debe fallar)
- Verificar que solo acepta enteros 1-7

## Instrucciones de Uso

Usa las herramientas de Playwright MCP:
1. `browser_navigate` - Para navegar a URLs
2. `browser_click` - Para hacer click en elementos
3. `browser_type` - Para escribir en inputs
4. `browser_snapshot` - Para capturar estado actual
5. `browser_take_screenshot` - Para evidencia visual

## Reporte
Al finalizar, genera un reporte con:
- Casos pasados vs fallados
- Screenshots de errores
- Logs de consola relevantes
