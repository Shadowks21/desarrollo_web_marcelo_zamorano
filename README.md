# Proyecto Híbrido: Flask + Spring Boot

Este proyecto implementa una aplicación web híbrida que combina:
- **Flask (Python)** - Para la interfaz principal y funcionalidades web
- **Spring Boot (Java)** - Para API avanzada y funcionalidades específicas

# La idea :c

## 🌐 URLs de Acceso

- **Flask App:** http://localhost:5000
- **Spring Boot App:** http://localhost:8080
- **API Java:** http://localhost:8080/api

## Funcionalidades

### Flask (Puerto 5000)
- Página principal con actividades
- Formulario agregar actividad
- Listado de actividades
- Detalle de actividad
- Comentarios
- Estadísticas básicas

### Spring Boot (Puerto 8080)
- API REST completa
- Paginación
- CORS configurado para Flask


### CORS
Spring Boot está configurado para aceptar peticiones desde Flask (localhost:5000).


## Integración

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

## Desarrollo

### Agregar Endpoints Java
1. Crear método en `ApiService`
2. Agregar endpoint en `ApiController`
3. Configurar CORS si es necesario

### Agregar Funcionalidades Flask
1. Crear ruta en `app.py`
2. Crear template si es necesario
3. Actualizar base de datos si es requerido

## Tecnologías Utilizadas

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


- **Dinamismo y experiencia de usuario:** Se priorizó el dinamismo en la interfaz, permitiendo que el usuario reciba retroalimentación inmediata sobre sus acciones (por ejemplo, mensajes de éxito o error al agregar actividades) y que la información se actualice automáticamente en pantalla.

# Conclusión 

En resumen, la Tarea 3 implicó la integración de conceptos modernos de desarrollo web, como AJAX, promesas y renderizado dinámico en el cliente, junto con una arquitectura clara y validaciones robustas, logrando una aplicación más interactiva, eficiente y correcta al momento de utilizarla.

Sin embargo, no se pudo realizar correctamente la tarea 4, pero si se lograron crear modelos en Java para la entrega. Así como también se utilzó la estructura vista en clases auxiliares.