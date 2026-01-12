package com.tarea4.model;

import jakarta.persistence.*;
import java.util.List;

/**
 * Entidad JPA que representa una Region de Chile.
 * Mapea la tabla 'region' de la base de datos.
 */
@Entity
@Table(name = "region")
public class Region {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @OneToMany(mappedBy = "region", fetch = FetchType.LAZY)
    private List<Comuna> comunas;

    // Constructores
    public Region() {
    }

    public Region(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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

    public List<Comuna> getComunas() {
        return comunas;
    }

    public void setComunas(List<Comuna> comunas) {
        this.comunas = comunas;
    }

    @Override
    public String toString() {
        return "Region{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
