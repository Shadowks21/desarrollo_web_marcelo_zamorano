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
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "actividad_id", nullable = false)
    private Integer actividadId;
    
    @Column(nullable = false)
    private Integer nota;
    
    @ManyToOne
    @JoinColumn(name = "actividad_id", insertable = false, updatable = false)
    private Actividad actividad;
    
    // Constructors
    public Nota() {}
    
    public Nota(Integer actividadId, Integer nota) {
        this.actividadId = actividadId;
        this.nota = nota;
    }
    
    // Getters and Setters
    public Integer getId() { 
        return id; 
    }
    
    public void setId(Integer id) { 
        this.id = id; 
    }
    
    public Integer getActividadId() { 
        return actividadId; 
    }
    
    public void setActividadId(Integer actividadId) { 
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

    @Repository
    public static interface NotaRepository extends JpaRepository<Nota, Integer> {

        // Obtener todas las notas de una actividad
        List<Nota> findByActividadId(Integer actividadId);

        // Calcular promedio de notas para una actividad
        @org.springframework.data.jpa.repository.Query("SELECT AVG(n.nota) FROM Nota n WHERE n.actividadId = :actividadId")
        Double findPromedioByActividadId(@Param("actividadId") Integer actividadId);

        // Contar total de notas para una actividad
        @Query("SELECT COUNT(n) FROM Nota n WHERE n.actividadId = :actividadId")
        Long countByActividadId(@Param("actividadId") Integer actividadId);
    }
}