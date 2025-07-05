# Proyecto Híbrido: Flask + Spring Boot

Este proyecto implementa una aplicación web híbrida que combina:
- **Flask (Python)** - Para la interfaz principal y funcionalidades web
- **Spring Boot (Java)** - Para API avanzada y funcionalidades específicas

## 🏗️ Arquitectura

```
┌─────────────────┐    ┌─────────────────┐
│   Flask App     │    │  Spring Boot    │
│   (Puerto 5000) │    │  (Puerto 8080)  │
│                 │    │                 │
│ • Interfaz Web  │    │ • API REST      │
│ • CRUD Básico   │    │ • Búsqueda Avz. │
│ • Estadísticas  │    │ • Filtros       │
│ • Comentarios   │    │ • Mapa          │
└─────────────────┘    └─────────────────┘
         │                       │
         └───────────────────────┘
                    │
         ┌─────────────────┐
         │   MySQL DB      │
         │   (tarea2)      │
         └─────────────────┘
```

## 🚀 Inicio Rápido

### Opción 1: Script Automático (Recomendado)

**Windows:**
```batch
# Ejecutar archivo batch
start_hybrid.bat

# O usando PowerShell
.\start_hybrid.ps1
```

**Linux/Mac:**
```bash
# Hacer ejecutable el script
chmod +x start_hybrid.sh

# Ejecutar
./start_hybrid.sh
```

### Opción 2: Manual

**1. Iniciar Flask:**
```bash
# Activar entorno virtual
source venvtareas/bin/activate  # Linux/Mac
# O: venvtareas\Scripts\activate  # Windows

# Instalar dependencias
pip install -r requirements.txt

# Ejecutar Flask
python app.py
```

**2. Iniciar Spring Boot:**
```bash
cd java/t4
./mvnw spring-boot:run  # Linux/Mac
# O: mvnw.cmd spring-boot:run  # Windows
```

## 🌐 URLs de Acceso

- **Flask App:** http://localhost:5000
- **Spring Boot App:** http://localhost:8080
- **API Java:** http://localhost:8080/api

## 📊 Funcionalidades

### Flask (Puerto 5000)
- ✅ Página principal con actividades
- ✅ Formulario agregar actividad
- ✅ Listado de actividades
- ✅ Detalle de actividad
- ✅ Comentarios
- ✅ Estadísticas básicas

### Spring Boot (Puerto 8080)
- ✅ API REST completa
- ✅ Búsqueda avanzada con filtros
- ✅ Paginación
- ✅ Endpoints estadísticos
- ✅ Interfaz web complementaria
- ✅ CORS configurado para Flask

## 📡 Endpoints API (Java)

### Actividades
- `GET /api/actividades/buscar?nombre={nombre}` - Buscar por nombre
- `GET /api/actividades/buscar-avanzado` - Búsqueda con filtros
- `GET /api/actividades/{id}` - Obtener por ID
- `GET /api/actividades/activas` - Actividades futuras
- `GET /api/actividades/comuna/{comunaId}` - Por comuna
- `GET /api/actividades/recientes?limit={n}` - Actividades recientes

### Estadísticas
- `GET /api/stats` - Estadísticas completas
- `GET /api/stats/total` - Total de actividades

### Utilidades
- `GET /api/mapa` - Datos para mapa
- `GET /api/health` - Health check

## 🔧 Configuración

### Base de Datos
Ambas aplicaciones usan la misma base de datos MySQL:
- **Host:** localhost:3306
- **Base de datos:** tarea2
- **Usuario:** cc5002
- **Contraseña:** programacionweb

### Puertos
- **Flask:** 5000
- **Spring Boot:** 8080

### CORS
Spring Boot está configurado para aceptar peticiones desde Flask (localhost:5000).

## 📁 Estructura del Proyecto

```
├── app.py                      # Aplicación Flask
├── requirements.txt            # Dependencias Python
├── start_hybrid.*             # Scripts de inicio
├── database/                  # Módulos de BD
├── static/                    # Recursos estáticos
├── templates/                 # Templates Flask
├── utils/                     # Utilidades
└── java/t4/                   # Proyecto Spring Boot
    ├── src/main/java/
    │   └── desarrollo/t4/t4/
    │       ├── controllers/   # Controladores
    │       ├── services/      # Servicios
    │       ├── models/        # Modelos JPA
    │       ├── repositories/  # Repositorios
    │       └── config/        # Configuración
    ├── src/main/resources/
    │   ├── templates/         # Templates Thymeleaf
    │   └── application.properties
    └── pom.xml               # Configuración Maven
```

## 🔀 Integración

### Flask → Java
Flask puede consumir la API de Java para:
- Búsquedas avanzadas
- Filtros complejos
- Estadísticas detalladas

### Java → Flask
Java puede redirigir a Flask para:
- Formularios de creación
- Interfaz principal
- Funcionalidades específicas

## 🛠️ Desarrollo

### Agregar Endpoints Java
1. Crear método en `ApiService`
2. Agregar endpoint en `ApiController`
3. Configurar CORS si es necesario

### Agregar Funcionalidades Flask
1. Crear ruta en `app.py`
2. Crear template si es necesario
3. Actualizar base de datos si es requerido

## 🧪 Testing

### Probar Flask
```bash
curl http://localhost:5000/
```

### Probar Spring Boot
```bash
curl http://localhost:8080/api/health
```

### Probar Integración
```bash
curl http://localhost:8080/api/actividades/buscar?nombre=test
```

## 📚 Tecnologías Utilizadas

### Frontend
- HTML5, CSS3, JavaScript
- Bootstrap 5
- Jinja2 (Flask templates)
- Thymeleaf (Spring Boot templates)

### Backend
- **Flask:** Python web framework
- **Spring Boot:** Java framework
- **JPA/Hibernate:** ORM para Java
- **SQLAlchemy:** ORM para Python

### Base de Datos
- MySQL 8.0
- Shared schema entre ambas apps

## 🐛 Solución de Problemas

### Puerto ocupado
```bash
# Verificar puertos en uso
netstat -ano | findstr :5000
netstat -ano | findstr :8080

# Matar procesos si es necesario
taskkill /PID <PID> /F
```

### Error de conexión BD
1. Verificar que MySQL esté corriendo
2. Verificar credenciales en `application.properties` y `db.py`
3. Verificar que la base de datos `tarea2` exista

### CORS Error
- Verificar configuración en `WebConfig.java`
- Verificar que Spring Boot esté corriendo en puerto 8080

## 📝 Notas

- Los scripts de inicio automático facilitan el desarrollo
- Ambas aplicaciones comparten la misma base de datos
- La configuración CORS permite integración fluida
- Cada aplicación mantiene su independencia funcional

- **Dinamismo y experiencia de usuario:** Se priorizó el dinamismo en la interfaz, permitiendo que el usuario reciba retroalimentación inmediata sobre sus acciones (por ejemplo, mensajes de éxito o error al agregar actividades) y que la información se actualice automáticamente en pantalla.

# Conclusión 

En resumen, la Tarea 3 implicó la integración de conceptos modernos de desarrollo web, como AJAX, promesas y renderizado dinámico en el cliente, junto con una arquitectura clara y validaciones robustas, logrando una aplicación más interactiva, eficiente y correcta al momento de utilizarla.