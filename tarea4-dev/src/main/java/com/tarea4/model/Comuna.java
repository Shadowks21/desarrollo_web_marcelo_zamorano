package com.tarea4.model;

import jakarta.persistence.*;
import java.util.List;

/**
 * Entidad JPA que representa una Comuna de Chile.
 * Mapea la tabla 'comuna' de la base de datos.
 */
@Entity
@Table(name = "comuna")
public class Comuna {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "region_id", insertable = false, updatable = false)
    private Long regionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @OneToMany(mappedBy = "comuna", fetch = FetchType.LAZY)
    private List<Actividad> actividades;

    // Constructores
    public Comuna() {
    }

    public Comuna(Long id, String nombre, Region region) {
        this.id = id;
        this.nombre = nombre;
        this.region = region;
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

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public List<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
    }

    @Override
    public String toString() {
        return "Comuna{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", regionId=" + regionId +
                '}';
    }
}
