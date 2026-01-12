---
name: t4-validate-css
description: Validar archivos CSS con W3C CSS Validator
user_invocable: true
---

# Validar CSS con W3C

Valida los archivos CSS del proyecto usando el validador oficial de W3C Jigsaw.

## Instrucciones

1. Identifica los archivos CSS a validar:
   - `src/main/resources/static/css/`
   - Estilos embebidos en HTML (si existen)

2. Para cada archivo CSS, usa el validador:
   - URL: https://jigsaw.w3.org/css-validator/validator
   - Parametros: ?uri=URL_DEL_ARCHIVO&output=json

3. Alternativa: Usa el validador web directamente
   - https://jigsaw.w3.org/css-validator/

4. Analiza los resultados y reporta:
   - Errores de sintaxis
   - Propiedades no validas
   - Valores incorrectos
   - Advertencias de compatibilidad

5. Sugiere correcciones para cada error

## Criterios de Validacion
- Sintaxis CSS3 valida
- Propiedades estandar (evitar prefijos vendor innecesarios)
- Valores dentro de rangos validos
- Selectores correctamente formados

## Penalizacion
- Cada error W3C descuenta 0.1 puntos en la evaluacion
