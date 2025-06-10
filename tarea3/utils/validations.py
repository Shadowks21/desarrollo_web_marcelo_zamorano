import re
import filetype

# Functions to validate activities within flask app

def validate_region(region):
    if region is None:
        return False
    return True

def validate_comuna(comuna):
    if comuna is None:
        return False
    return True

def validate_sector(sector):
    if sector is None:
        return True
    sector = sector.strip()
    b = bool(re.match(r'^[\w\sáéíóúÁÉÍÓÚñÑ]{1,100}$', sector))
    return b

def validate_nombre(nombre):
    if nombre is None:
        return False, "No existe nombre"
    nombre = nombre.strip()
    b = bool(re.match(r'^[\w\sáéíóúÁÉÍÓÚñÑ]{1,200}$', nombre))
    return b

def validate_email(email):
    if email is None:
        return False
    email = email.strip()
    b = bool(re.match(r'^[\w.+]+@[a-zA-Z_]+\.[a-zA-Z]{2,3}$', email))
    return b

def validate_celular(celular):
    if celular is None:
        return True
    celular = celular.strip()
    b = bool(re.match(r'^\+\d{3}\.\d{8}$$', celular))
    return b

def validate_contactar_por(contactar_por):
    if contactar_por is None:
        return False
    for contacto in contactar_por:
        if re.match(r'^[\w\sáéíóúÁÉÍÓÚñÑ]{4,50}$', contacto["identificador"].strip()) is not None:
            continue
        elif re.match(r'^[\w\sáéíóúÁÉÍÓÚñÑ]{4,50}$', contacto["identificador"].strip()) is None:
            return False
        elif re.match(r'^https?://[^\s/$.?#].[^\s]*$', contacto["identificador"].strip()) is None or len(contacto["identificador"].strip()) > 50 and len(contacto["identificador"].strip()) < 4:
            return False
    return True

from datetime import datetime

def validate_fecha_inicio(fecha_inicio_str):
    if not fecha_inicio_str:
        return False
    try:
        fecha_inicio = datetime.fromisoformat(fecha_inicio_str)
    except ValueError:
        return False
    ahora = datetime.now()
    if fecha_inicio < ahora:
        return False
    return True

def validate_fecha_termino(fecha_inicio_str, fecha_termino_str):
    if not fecha_inicio_str:
        return False
    if not fecha_termino_str:
        return True
    try:
        fecha_inicio = datetime.fromisoformat(fecha_inicio_str)
        fecha_termino = datetime.fromisoformat(fecha_termino_str)
    except ValueError:
        return False
    if fecha_termino < fecha_inicio:
        return False
    return True

def validate_descripcion(descripcion):
    if descripcion == "":
        return True
    descripcion = descripcion.strip()
    b = bool(re.match(r'^[\w\sáéíóúÁÉÍÓÚñÑ¿?¡!.,;:()"]{1,500}$', descripcion))
    return b

def validate_temas(tema):
    if tema is None:
        return False
    temas_permitidos = ["musica", "deporte", "ciencias", "religion", "politica", "tecnologia", "juegos",
                        "baile", "comida", "otro"]
    b = "-" in tema
    if tema.lower() not in temas_permitidos:
        if b:
            tema = tema.split("-")[0]
            b = bool(re.match(r'^[\w\sáéíóúÁÉÍÓÚñÑ.]{1,500}$', tema))
            if not b:
                return False
        else:
            return False
    return True

def validate_fotos(fotos):
    if fotos is None:
        return False
    for foto in fotos:
        if not filetype.is_image(foto):
            return False
        if not foto.filename.lower().endswith(('.png', '.jpg', '.jpeg')):
            return False
    return True

def validate_form(region, comuna, nombre, email, contactar_por, fecha_inicio_str, temas, fotos,
                   sector=None, descripcion=None, fecha_termino_str = None, celular=None):
    return all(
        [
            validate_region(region),
            validate_comuna(comuna),
            validate_sector(sector),
            validate_nombre(nombre),
            validate_email(email),
            validate_celular(celular),
            validate_contactar_por(contactar_por),
            validate_fecha_inicio(fecha_inicio_str),
            validate_fecha_termino(fecha_inicio_str, fecha_termino_str),
            validate_descripcion(descripcion),
            validate_temas(temas),
            validate_fotos(fotos)
        ]
    )