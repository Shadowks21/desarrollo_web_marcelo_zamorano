package com.tarea4.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa una Nota/Evaluacion de una Actividad.
 * Mapea la tabla 'nota' de la base de datos.
 * Las notas van del 1 al 7 (sistema de calificacion chileno).
 */
@Entity
@Table(name = "nota")
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "actividad_id", insertable = false, updatable = false)
    private Long actividadId;

    @Min(value = 1, message = "La nota minima es 1")
    @Max(value = 7, message = "La nota maxima es 7")
    @Column(name = "nota", nullable = false)
    private Integer nota;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actividad_id", nullable = false)
    private Actividad actividad;

    // Constructores
    public Nota() {
        this.fecha = LocalDateTime.now();
    }

    public Nota(Integer nota, Actividad actividad) {
        this.nota = nota;
        this.actividad = actividad;
        this.fecha = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActividadId() {
        return actividadId;
    }

    public void setActividadId(Long actividadId) {
        this.actividadId = actividadId;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    @Override
    public String toString() {
        return "Nota{" +
                "id=" + id +
                ", actividadId=" + actividadId +
                ", nota=" + nota +
                ", fecha=" + fecha +
                '}';
    }
}
