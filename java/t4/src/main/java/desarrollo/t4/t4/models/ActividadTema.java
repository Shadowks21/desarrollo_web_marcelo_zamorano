package desarrollo.t4.t4.models;

import jakarta.persistence.*;

@Entity
@Table(name = "actividad_tema")
public class ActividadTema {
    
    private @Id
    @SequenceGenerator(
        name = "actividad_tema_id_seq",
        sequenceName = "actividad_tema_id_seq",
        allocationSize = 1
    ) @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "actividad_tema_id_seq"
    ) Long id;
    
    @Column(nullable = false, length = 255)
    private String tema;
    
    @Column(name = "glosa_otro", length = 15)
    private String glosaOtro;
    
    @Column(name = "actividad_id", nullable = false)
    private Long actividadId;
    
    @ManyToOne
    @JoinColumn(name = "actividad_id", insertable = false, updatable = false)
    private Actividad actividad;
    
    // Constructors
    public ActividadTema() {}

    public ActividadTema(String tema, String glosaOtro, Long actividadId) {
        this.tema = tema;
        this.glosaOtro = glosaOtro;
        this.actividadId = actividadId;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    
    public String getTema() { return tema; }
    public void setTema(String tema) { this.tema = tema; }
    
    public String getGlosaOtro() { return glosaOtro; }
    public void setGlosaOtro(String glosaOtro) { this.glosaOtro = glosaOtro; }
    
    public Long getActividadId() { return actividadId; }
    public void setActividadId(Long actividadId) { this.actividadId = actividadId; }
    
    public Actividad getActividad() { return actividad; }
    public void setActividad(Actividad actividad) { this.actividad = actividad; }
}