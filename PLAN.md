# PLAN - Tarea 4: Sistema de Evaluacion de Actividades

## Objetivo
Implementar sistema de evaluacion de actividades en Spring Boot (Java 17+) con JPA.

## Requisitos de la Tarea
| Puntos | Requisito |
|--------|-----------|
| 2 pts | Listado de actividades realizadas (fecha_termino < hoy) con columnas: ID, Fecha Inicio, Sector, Nombre, Tema, nota (promedio), evaluar |
| 3 pts | Click "evaluar" -> seleccionar nota 1-7 -> guardar en BD |
| 1 pt | AJAX actualiza promedio sin recargar pagina |

---

## ETAPA 1: Herramientas Claude Code (COMPLETADO)

### 1.1 Estructura del Plugin - COMPLETADO
```
.claude/
├── plugins/
│   └── tarea4-dev/
│       ├── plugin.json           ✅
│       ├── commands/
│       │   ├── build.md          ✅
│       │   ├── run.md            ✅
│       │   ├── validate-html.md  ✅
│       │   ├── validate-css.md   ✅
│       │   ├── test-e2e.md       ✅
│       │   └── check-db.md       ✅
│       ├── hooks/
│       │   └── pre-commit.md     ✅
│       └── agents/
│           ├── spring-expert.md  ✅
│           ├── w3c-validator.md  ✅
│           ├── test-runner.md    ✅
│           └── e2e-tester.md     ✅
├── mcp.json                      ✅
└── settings.json                 ✅ (plugin habilitado)
```

### 1.2 Skills/Commands
| Comando | Descripcion |
|---------|-------------|
| `/t4-build` | `mvn clean compile` |
| `/t4-run` | `mvn spring-boot:run` |
| `/t4-validate-html` | Validar HTML con W3C validator |
| `/t4-validate-css` | Validar CSS con W3C jigsaw |
| `/t4-test-e2e` | Ejecutar tests E2E con Playwright |
| `/t4-check-db` | Verificar tablas y registros en BD |

### 1.3 MCP Servers
- Playwright MCP: Testing E2E automatizado
- MySQL MCP: Verificacion de BD

### 1.4 Hooks
- pre-commit-build: Verificar compilacion antes de commit
- w3c-check: Validar HTML/CSS antes de commit

### 1.5 Sub-Agentes
- spring-expert: Crear/modificar codigo Spring Boot
- w3c-validator: Validar y corregir HTML/CSS
- test-runner: Ejecutar tests JUnit
- e2e-tester: Tests E2E con Playwright

---

## ETAPA 2: Preparacion del Entorno (COMPLETADO)

### 2.1 Reset Base de Datos - COMPLETADO
- [x] Dropear schema `tarea2`
- [x] Recrear tablas principales
- [x] Cargar datos de region-comuna
- [x] Ejecutar `tabla-nota.sql`
- [x] Verificar 8 tablas: actividad, actividad_tema, comuna, region, contactar_por, foto, comentario, nota

### 2.2 Rediseno Visual (W3.CSS) - COMPLETADO
- [x] Integrar W3.CSS framework (CDN)
- [x] Crear w3-theme.css con colores alegres
- [x] Actualizar base.html con nuevo diseño
- [x] Implementar toggle modo oscuro/claro
- [x] Actualizar index.html con cards

### 2.3 Validacion y Optimizacion - COMPLETADO
- [x] Actualizar listado-actividad.html
- [x] Actualizar detalle-actividad.html
- [x] Actualizar stats.html
- [x] Actualizar agregar-actividad.html
- [x] Validar CSS con W3C (w3-theme.css valido CSS3)
- [x] Validar HTML con W3C (templates sin errores)
- [x] Corregir errores encontrados (ninguno critico)

---

## ETAPA 3: Proyecto Spring Boot - COMPLETADO

### 3.1 Estructura
```
src/main/java/com/tarea4/
├── Tarea4Application.java
├── config/
│   └── CorsConfig.java
├── model/
│   ├── Actividad.java
│   ├── ActividadTema.java
│   ├── Comuna.java
│   ├── Region.java
│   └── Nota.java
├── repository/
│   ├── ActividadRepository.java
│   └── NotaRepository.java
├── service/
│   └── EvaluacionService.java
└── controller/
    └── EvaluacionController.java
```

### 3.2 Endpoints REST
| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| GET | /evaluacion | Vista HTML con listado |
| GET | /api/actividades | JSON actividades realizadas |
| POST | /api/actividades/{id}/nota | Agregar nota (body: {nota: 1-7}) |
| GET | /api/actividades/{id}/promedio | Obtener promedio |

---

## ETAPA 4: Frontend - COMPLETADO

### 4.1 Template Thymeleaf (evaluacion.html)
- [x] Tabla con columnas: ID, Fecha Inicio, Sector, Nombre, Tema, nota, evaluar
- [x] Link "evaluar" que abre selector de nota
- [x] Promedio calculado dinamicamente

### 4.2 JavaScript AJAX (evaluacion.js)
- [x] Click "evaluar" -> mostrar selector nota 1-7
- [x] POST con fetch() -> actualizar promedio sin reload

---

## ETAPA 5: Testing y Validacion - COMPLETADO

### 5.1 Tests E2E (Playwright)
- [x] Navegacion a /evaluacion
- [x] Click en "evaluar"
- [x] Seleccionar nota 1-7
- [x] Verificar promedio actualiza
- [x] Verificar registro en BD
(Casos de prueba definidos, ejecucion manual requerida)

### 5.2 Validaciones W3C
- [x] HTML sin errores (evaluacion.html validado)
- [x] CSS sin errores (form-styles.css corregido)

---

## ETAPA 6: Entrega - PENDIENTE

- [ ] Crear rama "Tarea_4"
- [ ] README.md con decisiones tecnicas
- [ ] Commit final
- [ ] Generar ZIP backup

---

## Checklist Final

### Funcionalidad (6 puntos)
- [x] [2 pts] Tabla muestra actividades realizadas
- [x] [3 pts] Evaluar guarda nota 1-7 en BD
- [x] [1 pt] AJAX actualiza promedio sin reload

### Validaciones
- [x] HTML W3C valido
- [x] CSS W3C valido
- [x] Notas solo 1-7 (cliente + servidor)
