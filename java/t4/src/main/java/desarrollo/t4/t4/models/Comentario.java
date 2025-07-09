package desarrollo.t4.t4.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comentario")
public class Comentario {
    
    private @Id
    @SequenceGenerator(
        name = "comentario_id_seq",
        sequenceName = "comentario_id_seq",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.IDENTITY,
        generator = "comentario_id_seq"
    ) Long id;
    
    @Column(nullable = false, length = 200)
    private String nombre;
    
    @Column(nullable = false, length = 500)
    private String texto;
    
    @Column(nullable = false)
    private LocalDateTime fecha;
    
    @Column(name = "actividad_id", nullable = false)
    private Integer actividadId;
    
    @ManyToOne
    @JoinColumn(name = "actividad_id", insertable = false, updatable = false)
    private Actividad actividad;
    
    // Constructors
    public Comentario() {}

    public Comentario(String nombre, String texto, Integer actividadId) {
        this.nombre = nombre;
        this.texto = texto;
        this.actividadId = actividadId;
    }
    
    @PrePersist
    public void prePersist() {
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }
    }
    
    // Getters and Setters

    public Long getId() { return id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    
    public Integer getActividadId() { return actividadId; }
    public void setActividadId(Integer actividadId) { this.actividadId = actividadId; }
    
    public Actividad getActividad() { return actividad; }
    public void setActividad(Actividad actividad) { this.actividad = actividad; }

}