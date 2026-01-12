# Tarea 3 - Desarrollo Web - Marcelo Zamorano

## Descripción

Aplicación web para gestión de actividades recreativas chilenas, desarrollada con Flask, MySQL y JavaScript con AJAX.

## Instalación

### Requisitos
- Python 3.x
- MySQL Server

### Configuración de Base de Datos

1. Crear usuario de base de datos:
```bash
mysql -u root -p < database/create_user.sql
```

2. Crear esquema y cargar datos:
```bash
mysql -u cc5002 -p tarea2 < database/tarea2.sql
mysql -u cc5002 -p tarea2 < database/region-comuna.sql
mysql -u cc5002 -p tarea2 < database/tabla-comentario.sql
```

### Ejecutar Aplicación

```bash
pip install -r requirements.txt
python app.py
```

La aplicación estará disponible en `http://localhost:5000`

## Decisiones de Diseño

- **AJAX y Promesas:** Peticiones asíncronas para actualizar la interfaz sin recargar la página
- **Renderizado en cliente:** Gráficos generados con Chart.js desde datos obtenidos via AJAX
- **Separación de responsabilidades:** Backend (Flask) gestiona datos, frontend maneja interacción
- **Validaciones duales:** Validación en JavaScript (cliente) y Flask (servidor)
- **Rutas granulares:** Endpoints específicos para cada funcionalidad (`/stats/*`, `/actividad/<id>/comentario`)

## Estructura del Proyecto

```
├── app.py              # Rutas Flask y aplicación principal
├── database/           # Scripts SQL y modelos SQLAlchemy
├── static/
│   ├── css/            # Estilos
│   ├── js/             # JavaScript (AJAX, validaciones, gráficos)
│   └── uploads/        # Imágenes subidas por usuarios
├── templates/          # Plantillas Jinja2
└── utils/              # Validaciones servidor
```
