# Migración de Flask a Java Spring Boot - Tarea 5

## Descripción del Proyecto

Este proyecto representa la migración completa de una aplicación web desarrollada originalmente en Flask (Python) hacia Java Spring Boot. La aplicación gestiona un sistema de actividades y fotografías, permitiendo la administración de contenido multimedia asociado a diferentes eventos.

## Cambios Realizados en la Rama Tarea_5

### Migración de Arquitectura

La migración se fundamentó en los siguientes principios:

- **Cambio de framework**: De Flask a Spring Boot para aprovechar las ventajas del ecosistema Java
- **Arquitectura MVC**: Implementación del patrón Modelo-Vista-Controlador de manera más estructurada
- **Gestión de dependencias**: Migración de pip/requirements.txt a Maven
- **Persistencia de datos**: Integración con JPA/Hibernate para manejo de base de datos

### Estructura Principal (y nueva) del Proyecto

#### Modelos (Models)
- `Actividad.java`: Entidad que representa las actividades del sistema
- `Foto.java`: Entidad para gestión de fotografías asociadas a actividades

#### Repositorios (Repositories)
- `ActividadRepository.java`: Interface para operaciones CRUD de actividades
- `FotoRepository.java`: Interface para operaciones CRUD de fotografías

#### Servicios (Services)
- `AdminService.java`: Lógica de negocio para administración del sistema
- `AppService.java`: Servicios relacionados con la aplicació
- `ApiService.java`: Servicios API para interacción con la base de datos.

#### Controladores (Controllers)
- `AdminController.java`: Controlador para funcionalidades administrativas
- `AdminFotosController.java`: Controlador específico para administración de fotos
- `ActividadController.java`: Controlador para operaciones de actividades

### Decisiones Técnicas

#### Resumen
Se decidió migrar todo el proyecto de Flask integrado con python a Spring Boot integrado con Java, pues surgían ciertas colisiones
en la base de datos (creación de tablas seq y desconfiguración del proyecto en flask para añadir nuevas actividades).
Debido a esto se eliminó por completo la integración híbrida.

Se decidió aprovechar el cambio de framework para cambiar estilos, mejorar templates, seguridad y funcionalidades de la aplicación.
Trabajar con Java y el patrón Controller-Service-Repository (CSR) permite una mejor organización del código y una mayor escalabilidad.

La carpeta con las imagenes añadidas se carga dinámicamente desde el directorio `uploads/` ubicado dentro de la carpeta `target/classes/static/`.
Por favor, no recompile el proyecto si ha añadido imágenes, ya que se perderán al compilar. No se tuvó conocimiento de como configurar esto ya que
en la clase auxiliar no se habló de ello.

#### Gestión de Archivos
Se mantuvo la funcionalidad de carga y gestión de archivos multimedia, implementando:
- Almacenamiento en directorio `uploads/`
- Validación de tipos de archivo
- Generación de nombres únicos para evitar colisiones

#### Interfaz de Usuario
Se conservó la estructura de templates HTML con Thymeleaf como motor de plantillas:
- Fragmentos reutilizables
- Integración con Bootstrap para estilos
- JavaScript para funcionalidades dinámicas

#### Funcionalidades Implementadas
- **Funcionalidades anteriores**: Todas las funcionalidades de la versión Flask fueron migradas y mejoradas
- **Administración de fotos**: Visualización, eliminación y gestión por actividades

### Mejoras Implementadas

#### Seguridad
- Validación de entrada en controladores
- Manejo de excepciones estructurado
- Protección contra inyección de código
- Utilización de Spring Security para autenticación y autorización

#### Estilos
- Se mejoraron los estilos de los templates HTML utilizando Bootstrap.
- Utilización de nuevos iconos para dar dinamismo y cercanía a la interfaz de usuario.

#### Errores
- Mi proyecto funcionaba correctamente en la subida de imagenes. En la creación de un script para ejecutar el proyecto rápidamente, se desconfiguró todo, y ahora no funciona la subida de imagenes a la carpeta `uploads/`, pero si lo guarda en la base de datos. Dado que no se mencionó nada de esto en clase auxiliar, no se pudo solucionar.