---
name: t4-run
description: Ejecutar aplicacion Spring Boot
user_invocable: true
---

# Ejecutar Aplicacion Spring Boot

Inicia la aplicacion Spring Boot en modo desarrollo.

## Instrucciones

1. Verifica que el proyecto compile correctamente primero
2. Ejecuta el siguiente comando:

```bash
mvn spring-boot:run -f pom.xml
```

3. La aplicacion estara disponible en http://localhost:8080

4. Endpoints disponibles:
   - GET /evaluacion - Vista HTML con listado de actividades
   - GET /api/actividades - JSON con actividades realizadas
   - POST /api/actividades/{id}/nota - Agregar nota a actividad
   - GET /api/actividades/{id}/promedio - Obtener promedio de actividad

## Notas
- La aplicacion se conecta a MySQL en localhost:3306
- Base de datos: tarea2
- Usuario: cc5002 / Password: programacionweb
- Presiona Ctrl+C para detener la aplicacion
