# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Academic web project for managing Chilean recreational activities. Flask application with AJAX, JSON API endpoints, and comments system.

## Project Structure

```
├── app.py              # Flask routes and main application
├── database/           # SQL scripts and SQLAlchemy models
│   ├── db.py           # SQLAlchemy models and query functions
│   ├── create_user.sql # Database user setup
│   ├── tarea2.sql      # Main schema
│   ├── region-comuna.sql # Chilean regions/communes data
│   └── tabla-comentario.sql # Comments table
├── static/
│   ├── css/            # Stylesheets
│   ├── js/             # JavaScript files
│   └── uploads/        # User uploaded images
├── templates/          # Jinja2 templates (extend base.html)
├── utils/
│   └── validations.py  # Server-side form validation
└── requirements.txt    # Python dependencies
```

## Development Commands

```bash
pip install -r requirements.txt
python app.py
```

**Database setup** (MySQL required):
1. Create database user using `database/create_user.sql`
2. Run `database/tarea2.sql` and `database/region-comuna.sql`
3. Run `database/tabla-comentario.sql`

Database credentials: `cc5002` / `programacionweb` on `localhost:3306`, database `tarea2`

## Architecture

### Backend
- **app.py**: Flask routes for pages and form handling
- **database/db.py**: SQLAlchemy models (Actividad, Comuna, Region, Contactar_por, Foto, Comentario) and query functions
- **utils/validations.py**: Server-side form validation
- **templates/**: Jinja2 templates (extend `base.html`)

### API Endpoints
- JSON API endpoints at `/stats/*` with CORS for client-side chart rendering
- Comments system (`Comentario` model, `/actividad/<id>/comentario` route)
- Client-side AJAX for dynamic updates without page reloads

### Frontend
- **form-validation.js**: Client-side validation
- **agregar.js**: Dynamic form elements (images, contacts up to 5 each)
- **region_comuna.js**: Chilean regions/communes dataset (~1600 entries)
- **stats.js**: Chart.js 4 with AJAX data fetching

## Key Conventions

- **Spanish language**: All code identifiers and UI text in Spanish
- **Dark theme**: #0d1117 background, #03e9f4 accent (defined in `form-styles.css`)
- **Custom themes stored with "-" suffix**: When tema="otro", value is stored as `"{custom_value}-"` to distinguish from predefined themes
- **File uploads**: SHA256 hashed filenames, stored in `static/uploads/`, max 16MB, allowed: png/jpg/jpeg
