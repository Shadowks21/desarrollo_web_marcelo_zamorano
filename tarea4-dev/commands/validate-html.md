---
name: t4-validate-html
description: Validar archivos HTML con W3C Validator
user_invocable: true
---

# Validar HTML con W3C

Valida los archivos HTML del proyecto usando el validador oficial de W3C.

## Instrucciones

1. Identifica los archivos HTML a validar:
   - Templates Thymeleaf en `src/main/resources/templates/`
   - Archivos HTML estaticos

2. Para cada archivo HTML, usa WebFetch para validar:
   - URL: https://validator.w3.org/nu/?out=json
   - Metodo: POST con el contenido HTML

3. Alternativa: Usa el validador web directamente
   - https://validator.w3.org/

4. Analiza los resultados y reporta:
   - Errores encontrados (con linea y descripcion)
   - Advertencias
   - Sugerencias de correccion

5. Prioriza corregir errores antes que advertencias

## Criterios de Validacion
- DOCTYPE html5 declarado
- Tags correctamente cerrados
- Atributos validos
- Estructura semantica correcta
- Accesibilidad basica (alt en imagenes, labels en forms)

## Penalizacion
- Cada error W3C descuenta 0.1 puntos en la evaluacion
