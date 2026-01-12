import re
import filetype
from datetime import datetime

def validate_region(region):
    if region == "":
        return False, "La región es obligatoria"
    return True, ""

def validate_comuna(comuna):
    if comuna == "":
        return False, "La comuna es obligatoria"
    return True, ""

def validate_sector(sector):
    if sector == "":
        return True, ""
    sector = sector.strip()
    b = bool(re.match(r'^[\w\sáéíóúÁÉÍÓÚñÑ]{1,100}$', sector))
    if not b:
        return False, "El sector debe contener máximo 100 caracteres alfanuméricos"
    return True, ""

def validate_nombre(nombre):
    if nombre == "":
        return False, "El nombre es obligatorio"
    nombre = nombre.strip()
    b = bool(re.match(r'^[\w\sáéíóúÁÉÍÓÚñÑ]{1,200}$', nombre))
    if not b:
        return False, "El nombre debe contener máximo 200 caracteres alfanuméricos"
    return True, ""

def validate_email(email):
    if email == "":
        return False, "El email es obligatorio"
    email = email.strip()
    b = bool(re.match(r'^[\w.+]+@[a-zA-Z_]+\.[a-zA-Z]{2,3}$', email))
    if not b:
        return False, "El formato del email es inválido"
    return True, ""

def validate_celular(celular):
    if celular == "":
        return True, ""
    celular = celular.strip()
    b = bool(re.match(r'^\+\d{3}\.\d{8}$', celular))
    if not b:
        return False, "El formato del celular debe ser +XXX.XXXXXXXX"
    return True, ""

def validate_contactar_por(contactar_por):
    if contactar_por == "":
        return False, "Debe especificar al menos un medio de contacto"
    for contacto in contactar_por:
        identificador = contacto["identificador"].strip()
        if re.match(r'^[\w\sáéíóúÁÉÍÓÚñÑ]{4,50}$', identificador) == "":
            if re.match(r'^https?://[^\s/$.?#].[^\s]*$', identificador) == "" or len(identificador) > 50 or len(identificador) < 4:
                return False, "El identificador de contacto debe tener entre 4 y 50 caracteres válidos"
    return True, ""

def validate_fecha_inicio(fecha_inicio_str):
    if not fecha_inicio_str:
        return False, "La fecha de inicio es obligatoria"
    try:
        fecha_inicio = datetime.fromisoformat(fecha_inicio_str)
    except ValueError:
        return False, "El formato de fecha de inicio no es válido"
    ahora = datetime.now()
    if fecha_inicio < ahora:
        return False, "La fecha de inicio debe ser futura"
    return True, ""

def validate_fecha_termino(fecha_inicio_str, fecha_termino_str):
    if not fecha_inicio_str:
        return False, "La fecha de inicio es obligatoria"
    if not fecha_termino_str:
        return True, ""
    try:
        fecha_inicio = datetime.fromisoformat(fecha_inicio_str)
        fecha_termino = datetime.fromisoformat(fecha_termino_str)
    except ValueError:
        return False, "El formato de fecha no es válido"
    if fecha_termino < fecha_inicio:
        return False, "La fecha de término debe ser posterior a la fecha de inicio"
    return True, ""

def validate_descripcion(descripcion):
    if descripcion == "":
        return True, ""
    descripcion = descripcion.strip()
    b = bool(re.match(r'^[\w\sáéíóúÁÉÍÓÚñÑ¿?¡!.,;:()"]{1,500}$', descripcion))
    if not b:
        return False, "La descripción debe tener máximo 500 caracteres válidos"
    return True, ""

def validate_temas(tema):
    if tema == "":
        return False, "Debe seleccionar al menos un tema"
    temas_permitidos = ["musica", "deporte", "ciencias", "religion", "politica", "tecnologia", "juegos",
                        "baile", "comida", "otro"]
    b = "-" in tema
    if tema.lower() not in temas_permitidos:
        if b:
            tema_base = tema.split("-")[0]
            b = bool(re.match(r'^[\w\sáéíóúÁÉÍÓÚñÑ.]{1,500}$', tema_base))
            if not b:
                return False, "El tema especificado no es válido"
        else:
            return False, "El tema debe estar entre los permitidos"
    return True, ""

def validate_fotos(fotos):
    if fotos == "":
        return False, "Debe incluir al menos una foto"
    for foto in fotos:
        if not filetype.is_image(foto):
            return False, f"El archivo {foto.filename} no es una imagen válida"
        if not foto.filename.lower().endswith(('.png', '.jpg', '.jpeg')):
            return False, "Solo se permiten imágenes PNG, JPG o JPEG"
    return True, ""

def validate_form(region, comuna, nombre, email, contactar_por, fecha_inicio_str, temas, fotos,
                   sector="", descripcion="", fecha_termino_str="", celular=""):
    validations = [
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

    results = [v[0] for v in validations]
    messages = [v[1] for v in validations if v[1] != ""]

    return {
        "verified": all(results),
        "error_messages": messages
    }