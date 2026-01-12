package com.tarea4.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad JPA que representa una Actividad recreativa.
 * Mapea la tabla 'actividad' de la base de datos.
 */
@Entity
@Table(name = "actividad")
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "celular", length = 15)
    private String celular;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "dia_hora_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "dia_hora_termino")
    private LocalDateTime fechaTermino;

    @Column(name = "sector", length = 100)
    private String sector;

    @Column(name = "comuna_id", insertable = false, updatable = false)
    private Long comunaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comuna_id")
    private Comuna comuna;

    @OneToMany(mappedBy = "actividad", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ActividadTema> temas;

    @OneToMany(mappedBy = "actividad", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Nota> notas;

    // Constructores
    public Actividad() {
    }

    public Actividad(String nombre, String email, LocalDateTime fechaInicio) {
        this.nombre = nombre;
        this.email = email;
        this.fechaInicio = fechaInicio;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaTermino() {
        return fechaTermino;
    }

    public void setFechaTermino(LocalDateTime fechaTermino) {
        this.fechaTermino = fechaTermino;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public Long getComunaId() {
        return comunaId;
    }

    public void setComunaId(Long comunaId) {
        this.comunaId = comunaId;
    }

    public Comuna getComuna() {
        return comuna;
    }

    public void setComuna(Comuna comuna) {
        this.comuna = comuna;
    }

    public List<ActividadTema> getTemas() {
        return temas;
    }

    public void setTemas(List<ActividadTema> temas) {
        this.temas = temas;
    }

    public List<Nota> getNotas() {
        return notas;
    }

    public void setNotas(List<Nota> notas) {
        this.notas = notas;
    }

    /**
     * Obtiene el tema principal de la actividad.
     * Si tiene glosa_otro, retorna ese valor; sino, retorna el tema.
     */
    public String getTemaDisplay() {
        if (temas != null && !temas.isEmpty()) {
            ActividadTema tema = temas.get(0);
            if (tema.getGlosaOtro() != null && !tema.getGlosaOtro().isEmpty()) {
                return tema.getGlosaOtro();
            }
            return tema.getTema();
        }
        return "Sin tema";
    }

    @Override
    public String toString() {
        return "Actividad{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaTermino=" + fechaTermino +
                ", sector='" + sector + '\'' +
                '}';
    }
}
