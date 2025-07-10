package desarrollo.t4.t4.models;

import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Entity
@Table(name = "nota")
public class Nota {
    
    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

    @Column(name = "actividad_id", nullable = false)
    private Long actividadId;
    
    @Column(nullable = false)
    private Integer nota;
    
    @ManyToOne
    @JoinColumn(name = "actividad_id", nullable = false, insertable = false, updatable = false)
    private Actividad actividad;
    
    // Constructors
    public Nota() {}
    
    public Nota(Actividad actividad, Integer nota) {
        this.actividad = actividad;
        this.actividadId = actividad.getId();
        this.nota = nota;
    }

    // Getters and Setters

    public Long getId() {
        return id;
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
    
    public Actividad getActividad() { 
        return actividad; 
    }
    
    public void setActividad(Actividad actividad) { 
        this.actividad = actividad; 
    }

}