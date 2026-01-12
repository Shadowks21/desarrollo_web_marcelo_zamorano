package com.tarea4.model;

import jakarta.persistence.*;

/**
 * Entidad JPA que representa el tema de una Actividad.
 * Mapea la tabla 'actividad_tema' de la base de datos.
 */
@Entity
@Table(name = "actividad_tema")
public class ActividadTema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tema", nullable = false, length = 255)
    private String tema;

    @Column(name = "glosa_otro", length = 15)
    private String glosaOtro;

    @Column(name = "actividad_id", insertable = false, updatable = false)
    private Long actividadId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actividad_id")
    private Actividad actividad;

    // Constructores
    public ActividadTema() {
    }

    public ActividadTema(String tema, Actividad actividad) {
        this.tema = tema;
        this.actividad = actividad;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getGlosaOtro() {
        return glosaOtro;
    }

    public void setGlosaOtro(String glosaOtro) {
        this.glosaOtro = glosaOtro;
    }

    public Long getActividadId() {
        return actividadId;
    }

    public void setActividadId(Long actividadId) {
        this.actividadId = actividadId;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    @Override
    public String toString() {
        return "ActividadTema{" +
                "id=" + id +
                ", tema='" + tema + '\'' +
                ", glosaOtro='" + glosaOtro + '\'' +
                ", actividadId=" + actividadId +
                '}';
    }
}
