package desarrollo.t4.t4.models;

import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "foto")
public class Foto {
    
    private @Id
    @SequenceGenerator(
        name = "foto_id_seq",
        sequenceName = "foto_id_seq",
        allocationSize = 1
    ) @GeneratedValue(
        strategy = GenerationType.IDENTITY,
        generator = "foto_id_seq"
    ) Long id;
    
    @Column(name = "ruta_archivo", nullable = false, length = 300)
    private String rutaArchivo;
    
    @Column(name = "nombre_archivo", nullable = false, length = 300)
    private String nombreArchivo;
    
    @Column(name = "actividad_id", nullable = false)
    private Long actividadId;
    
    @ManyToOne
    @JoinColumn(name = "actividad_id", insertable = false, updatable = false)
    private Actividad actividad;
    
    // Constructors
    public Foto() {}
    
    // Getters and Setters

    public Long getId() { return id; }
    
    public String getRutaArchivo() { return rutaArchivo; }
    public void setRutaArchivo(String rutaArchivo) { this.rutaArchivo = rutaArchivo; }
    
    public String getNombreArchivo() { return nombreArchivo; }
    public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }
    
    public Long getActividadId() { return actividadId; }
    public void setActividadId(Long actividadId) { this.actividadId = actividadId; }
    
    public Actividad getActividad() { return actividad; }
    public void setActividad(Actividad actividad) { this.actividad = actividad; }

}