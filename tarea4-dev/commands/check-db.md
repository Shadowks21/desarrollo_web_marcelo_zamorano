---
name: t4-check-db
description: Verificar estado de la base de datos MySQL
user_invocable: true
---

# Verificar Base de Datos

Verifica el estado de la base de datos MySQL y la integridad de los datos.

## Pre-requisitos
- MySQL corriendo en localhost:3306
- MCP MySQL configurado
- Base de datos: tarea2
- Usuario: cc5002 / Password: programacionweb

## Verificaciones

### 1. Verificar tablas existentes
```sql
SHOW TABLES;
```

Tablas esperadas:
- actividad
- actividad_tema
- comuna
- region
- contactar_por
- foto
- comentario
- nota (nueva para T4)

### 2. Verificar estructura tabla nota
```sql
DESCRIBE nota;
```

Campos esperados:
- id INT AUTO_INCREMENT PRIMARY KEY
- actividad_id INT NOT NULL (FK)
- nota INT NOT NULL

### 3. Verificar actividades realizadas
```sql
SELECT COUNT(*) FROM actividad WHERE dia_hora_termino < NOW();
```

### 4. Verificar notas registradas
```sql
SELECT a.id, a.nombre, COUNT(n.id) as total_notas, AVG(n.nota) as promedio
FROM actividad a
LEFT JOIN nota n ON a.id = n.actividad_id
WHERE a.dia_hora_termino < NOW()
GROUP BY a.id, a.nombre;
```

### 5. Verificar integridad referencial
```sql
-- Notas sin actividad valida (no deberia haber)
SELECT * FROM nota WHERE actividad_id NOT IN (SELECT id FROM actividad);
```

## Instrucciones de Uso

Usa las herramientas de MySQL MCP:
1. `query` - Ejecutar consultas SELECT
2. `list_tables` - Listar tablas
3. `describe_table` - Ver estructura de tabla

## Reporte
Genera un resumen con:
- Estado de cada tabla
- Cantidad de registros
- Problemas de integridad encontrados
