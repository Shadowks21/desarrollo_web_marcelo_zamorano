import pymysql
import json
from sqlalchemy import create_engine, Column, Integer, String, ForeignKey, DateTime, func, case
from sqlalchemy.orm import sessionmaker, declarative_base, relationship

DB_NAME = "tarea2"
DB_USERNAME = "cc5002"
DB_PASSWORD = "programacionweb"
DB_HOST = "localhost"
DB_PORT = 3306
DB_CHARSET = "utf8"

DATABASE_URL = f"mysql+pymysql://{DB_USERNAME}:{DB_PASSWORD}@{DB_HOST}:{DB_PORT}/{DB_NAME}"

engine = create_engine(DATABASE_URL, echo=False, future=True)
SessionLocal = sessionmaker(bind=engine)

Base = declarative_base()

# -- Models --

class Actividad(Base):
    __tablename__ = "actividad"
    id = Column(Integer, primary_key=True, autoincrement=True)
    comuna_id = Column(Integer, ForeignKey("comuna.id"))
    sector = Column(String(100), nullable=True)
    nombre = Column(String(200), nullable=False)
    email = Column(String(100), nullable=False)
    celular = Column(String(15), nullable=True)
    dia_hora_inicio = Column(DateTime, nullable=False)
    dia_hora_termino = Column(DateTime, nullable=True)
    descripcion = Column(String(255), nullable=True)

    comuna = relationship("Comuna", back_populates="actividades")
    tema = relationship("Actividad_tema", back_populates="actividad")
    contactar_por = relationship("Contactar_por", back_populates="actividad")
    fotos = relationship("Foto", back_populates="actividad")
    comentarios = relationship("Comentario", back_populates="actividad")

class Actividad_tema(Base):
    __tablename__ = "actividad_tema"
    id = Column(Integer, primary_key=True, autoincrement=True)
    tema = Column(String(255), nullable=False)
    glosa_otro = Column(String(15), nullable=True)
    actividad_id = Column(Integer, ForeignKey("actividad.id"), primary_key=True)

    actividad = relationship("Actividad", back_populates="tema")

class Comuna(Base):
    __tablename__ = "comuna"
    id = Column(Integer, primary_key=True, autoincrement=False)
    nombre = Column(String(200), nullable=False)
    region_id = Column(Integer, ForeignKey("region.id"), nullable=False)

    region = relationship("Region", back_populates="comunas")
    actividades = relationship("Actividad", back_populates="comuna")
    
class Region(Base):
    __tablename__ = "region"
    id = Column(Integer, primary_key=True, autoincrement=True)
    nombre = Column(String(200), nullable=False)

    comunas = relationship("Comuna", back_populates="region")

class Contactar_por(Base):
    __tablename__ = "contactar_por"
    id = Column(Integer, primary_key=True, autoincrement=True)
    nombre = Column(String(255), nullable=False)
    identificador = Column(String(150), nullable=False)
    actividad_id = Column(Integer, ForeignKey("actividad.id"), primary_key=True)

    actividad = relationship("Actividad", back_populates="contactar_por")

class Foto(Base):
    __tablename__ = "foto"
    id = Column(Integer, primary_key=True, autoincrement=True)
    ruta_archivo = Column(String(300), nullable=False)
    nombre_archivo = Column(String(300), nullable=False)
    actividad_id = Column(Integer, ForeignKey("actividad.id"), primary_key=True)

    actividad = relationship("Actividad", back_populates="fotos")

class Comentario(Base):
    __tablename__ = "comentario"
    id = Column(Integer, primary_key=True, autoincrement=True)
    nombre = Column(String(200), nullable=False)
    texto = Column(String(500), nullable=False)
    fecha = Column(DateTime, nullable=False, default=func.now())
    actividad_id = Column(Integer, ForeignKey("actividad.id"), nullable=False)

    actividad = relationship("Actividad", back_populates="comentarios")

# -- Get connection --

def get_conn():
    conn = pymysql.connect(
        host=DB_HOST,
        user=DB_USERNAME,
        password=DB_PASSWORD,
        db=DB_NAME,
        port=DB_PORT,
        charset=DB_CHARSET,
    )
    return conn

# -- Database Functions --

def get_activities(page = 1, page_size = 5):
    session = SessionLocal()
    actividades = session.query(Actividad).order_by(Actividad.id.desc()).limit(page_size).all()
    data = []
    if actividades:
        for actividad in actividades:
            act = get_activity_by_id(actividad.id)
            comuna = get_comuna_by_id(actividad.comuna_id)
            tema = get_tema_by_id(actividad.tema[0].id)
            tema_tema = tema.tema
            if tema.glosa_otro:
                tema_tema = tema.glosa_otro
            fotos = get_fotos_by_activity_id(actividad.id)
            fotos = [f.ruta_archivo for f in fotos]
            if act:
                data.append({
                    "id": actividad.id,
                    "inicio": actividad.dia_hora_inicio,
                    "termino": actividad.dia_hora_termino,
                    "comuna": comuna.nombre,
                    "sector": actividad.sector,
                    "tema": tema_tema,
                    "imagen": fotos,
                })
    session.close()
    return data

def get_activities_for_list(page=1, page_size=5):
    session = SessionLocal()
    actividades = session.query(Actividad).order_by(Actividad.id.desc()).offset((page - 1) * page_size).limit(page_size).all()
    data = []
    total = session.query(Actividad).count()
    for actividad in actividades:
        act = get_activity_by_id(actividad.id)
        comuna = get_comuna_by_id(actividad.comuna_id)
        tema = get_tema_by_id(actividad.tema[0].id)
        tema_tema = tema.tema
        if tema.glosa_otro:
            tema_tema = tema.glosa_otro
        fotos = get_fotos_by_activity_id(actividad.id)
        fotos = [f.ruta_archivo for f in fotos]        
        if act:
            data.append({
                "id": actividad.id,
                "descripcion": actividad.descripcion,
                "fecha_inicio": actividad.dia_hora_inicio,
                "fecha_termino": actividad.dia_hora_termino,
                "comuna": comuna.nombre,
                "sector": actividad.sector,
                "tema": tema_tema,
                "nombre_organizador": actividad.nombre,
                "total_fotos": len(fotos),
            })
    session.close()
    return data, total

def save_activity(comuna, sector, nombre, email, celular, contactar_por, fecha_inicio_str,
                  fecha_termino_str, descripcion, tema, fotos):
    session = SessionLocal()
    actividad = Actividad(
        comuna_id=comuna,
        sector=sector,
        nombre=nombre,
        email=email,
        celular=celular,
        dia_hora_inicio=fecha_inicio_str,
        dia_hora_termino=fecha_termino_str,
        descripcion=descripcion
    )
    session.add(actividad)
    session.commit()
    session.refresh(actividad)

    bool = "-" in tema
    actividad_tema = Actividad_tema(
        tema="otro" if bool else tema,
        glosa_otro=tema.split("-")[0] if bool else None,
        actividad_id=actividad.id
    )
    session.add(actividad_tema)
    for dic in contactar_por:
        contacto = Contactar_por(
            nombre=dic["tipo"],
            identificador=dic["identificador"],
            actividad_id=actividad.id
        )
        session.add(contacto)

    for foto in fotos:
        foto_obj = Foto(
            ruta_archivo=f"uploads/{foto}",
            nombre_archivo=foto,
            actividad_id=actividad.id
        )
        session.add(foto_obj)
    id = actividad.id
    session.commit()
    session.close()
    print("Actividad guardada con éxito")
    return id

def get_activity_by_id(activity_id):
    session = SessionLocal()
    actividad = session.query(Actividad).filter_by(id = activity_id).first()
    session.close()
    return actividad

def get_details_activity_by_id(activity_id):
    session = SessionLocal()
    actividad = session.query(Actividad).filter_by(id = activity_id).first()
    if not actividad:
        session.close()
        return None
    comuna = get_comuna_by_id(actividad.comuna_id)
    tema = get_tema_by_id(actividad.tema[0].id)
    tema_tema = tema.tema
    if tema.glosa_otro:
        tema_tema = tema.glosa_otro
    fotos = get_fotos_by_activity_id(actividad.id)
    fotos = [f.ruta_archivo for f in fotos]
    total_fotos = len(fotos)
    despripcion = actividad.descripcion
    fecha_inicio = actividad.dia_hora_inicio
    fecha_termino = actividad.dia_hora_termino
    sector = actividad.sector
    nombre_organizador = actividad.nombre
    session.close()
    return {
        "id": actividad.id,
        "descripcion": despripcion,
        "fecha_inicio": fecha_inicio,
        "fecha_termino": fecha_termino,
        "comuna": comuna.nombre,
        "sector": sector,
        "tema": tema_tema,
        "nombre_organizador": nombre_organizador,
        "total_fotos": total_fotos,
        "imagenes": fotos
    }

def get_comuna_by_id(comuna_id):
    session = SessionLocal()
    comuna = session.query(Comuna).filter_by(id = comuna_id).first()
    session.close()
    return comuna

def get_tema_by_id(tema_id):
    session = SessionLocal()
    tema = session.query(Actividad_tema).filter_by(id = tema_id).first()
    session.close()
    return tema

def get_fotos_by_activity_id(activity_id):
    session = SessionLocal()
    fotos = session.query(Foto).filter_by(actividad_id = activity_id).all()
    session.close()
    return fotos

def get_activities_by_day():
    session = SessionLocal()
    actividades = session.query(
        func.date(Actividad.dia_hora_inicio).label("fecha"),
        func.count(Actividad.id).label("total")
    ).group_by(func.date(Actividad.dia_hora_inicio)).all()
    data = []
    for actividad in actividades:
        data.append({
            "fecha": actividad.fecha.strftime("%Y-%m-%d"),
            "total": actividad.total
        })
    session.close()
    return data

def get_activities_by_type():
    session = SessionLocal()
    actividades = session.query(
        Actividad_tema.tema,
        func.count(Actividad.id).label("total")
    ).join(Actividad, Actividad_tema.actividad_id == Actividad.id) \
    .group_by(Actividad_tema.tema).all()
    data = []
    for actividad in actividades:
        data.append({
            "tema": actividad.tema,
            "total": actividad.total
        })
    session.close()
    return data

def get_activities_by_schedule():
    session = SessionLocal()
    actividades = session.query(
        func.date_format(Actividad.dia_hora_inicio, '%Y-%m').label("mes"),
        func.sum(case((func.hour(Actividad.dia_hora_inicio) < '12', 1), else_=0)).label('manana'),
        func.sum(case((func.hour(Actividad.dia_hora_inicio).between('12', '17'), 1), else_=0)).label('mediodia'),
        func.sum(case((func.hour(Actividad.dia_hora_inicio) >= '18', 1), else_=0)).label('tarde')
    ).group_by(func.date_format(Actividad.dia_hora_inicio, '%Y-%m')).order_by(func.date_format(Actividad.dia_hora_inicio, '%Y-%m')).all()
    data = []
    for actividad in actividades:
        data.append({
            "mes": actividad.mes,
            "manana": actividad.manana,
            "mediodia": actividad.mediodia,
            "tarde": actividad.tarde
        })
    session.close()
    return data
    