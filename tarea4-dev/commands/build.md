---
name: t4-build
description: Compilar proyecto Spring Boot con Maven
user_invocable: true
---

# Compilar Proyecto Spring Boot

Ejecuta la compilacion del proyecto Maven para verificar que no hay errores de sintaxis o dependencias.

## Instrucciones

1. Navega al directorio del proyecto Spring Boot
2. Ejecuta el siguiente comando:

```bash
mvn clean compile -f pom.xml
```

3. Si hay errores de compilacion, analiza el output y reporta:
   - Archivo con error
   - Linea del error
   - Descripcion del problema
   - Sugerencia de solucion

4. Si la compilacion es exitosa, confirma al usuario que el proyecto compila correctamente.

## Notas
- Asegurate de que Maven este instalado en el sistema
- El proyecto debe tener un archivo pom.xml valido
- Java 17+ es requerido
