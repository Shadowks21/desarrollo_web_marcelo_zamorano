from flask import Flask, request, render_template, redirect, url_for, session
from database import db
from utils.validations import *
from datetime import datetime
from werkzeug.utils import secure_filename
import hashlib
import filetype
import os

UPLOAD_FOLDER = "static/uploads"

app = Flask(__name__)

app.secret_key = "s3cr3t_k3y"
app.config["UPLOAD_FOLDER"] = UPLOAD_FOLDER
app.config["MAX_CONTENT_LENGTH"] = 16 * 1024 * 1024  # 16 MB
ALLOWED_EXTENSIONS = {"png", "jpg", "jpeg"}

# -- Routes --
@app.route("/", methods=["GET"])
def index():
    data = db.get_activities()
    return render_template("index.html", data=data)

@app.route("/formulario", methods=["GET"])
def formulario():
    if request.method == "GET":
        return render_template("agregar-actividad.html")
    
@app.route("/agregar_actividad", methods=["GET","POST"])
def agregar_actividad():
    if request.method == "POST":
        region = request.form.get("select-region")
        comuna = request.form.get("select-comuna")
        sector = request.form.get("sector")
        nombre = request.form.get("nombre")
        email = request.form.get("email")
        celular = request.form.get("telefono")
        tipos = request.form.getlist("contacto-tipo")
        identifcadores = []
        for i in range (1, len(tipos) + 1):
            valor = request.form.get(f"contacto-identificador-{i}")
            if valor:
                identifcadores.append(valor)
        contactos = []
        for tipo, identificador in zip(tipos, identifcadores):
            contactos.append({
                "tipo": tipo,
                "identificador": identificador
            })
        fecha_inicio_str = request.form.get("fecha-y-hora-inicio")
        fecha_termino_str = request.form.get("fecha-y-hora-fin")
        fecha_inicio = datetime.fromisoformat(fecha_inicio_str)
        fecha_termino = None
        if fecha_termino_str:
            fecha_termino = datetime.fromisoformat(fecha_termino_str)
        descripcion = request.form.get("descripcion")
        tema = request.form.get("tema") if request.form.get("tema") != "otro" else (request.form.get("tema-otro") + "-")
        fotos = request.files.getlist("imagenes")

        # Validar los datos del formulario
        if not validate_form(region, comuna, nombre, email, contactos, fecha_inicio_str, tema, fotos,
                             sector, descripcion, fecha_termino_str, celular):
            print("Error en la validación de datos")
            return render_template("agregar-actividad.html", error="Error en la validación de datos, porfavor revise los datos ingresados")

        fotos_guardadas = []
        for foto in fotos:
            _filename = hashlib.sha256(
            secure_filename(foto.filename) # nombre del archivo
            .encode("utf-8") # encodear a bytes
            ).hexdigest()
            _extension = filetype.guess(foto).extension
            img_filename = f"{_filename}.{_extension}"
            foto.save(os.path.join(app.config["UPLOAD_FOLDER"], img_filename))
            fotos_guardadas.append(img_filename)

        # Guardar la actividad en la base de datos
        db.save_activity(comuna, sector, nombre, email, celular, contactos, fecha_inicio,
                                         fecha_termino, descripcion, tema, fotos_guardadas)
        return redirect(url_for("index"))

    return render_template("agregar-actividad.html")

@app.route("/listar_actividades", methods=["GET"])
def listar_actividades():
    page = int(request.args.get("page", 1))
    page_size = int(request.args.get("page_size", 5))
    data, total = db.get_activities_for_list(page=page, page_size=page_size)
    return render_template("listado-actividad.html", data=data, page=page, total=total, page_size=page_size)

@app.route("/actividad/<int:id>", methods=["GET"])
def detalle_actividad(id):
    actividad = db.get_details_activity_by_id(id)
    if not actividad:
        return redirect(url_for("listar_actividades"))
    return render_template("detalle-actividad.html", actividad=actividad)

@app.route("/stats", methods=["GET"])
def stats():
    return render_template("stats.html")