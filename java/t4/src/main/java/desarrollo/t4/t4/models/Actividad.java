package desarrollo.t4.t4.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List; 

@Entity
@Table(name = "actividad")
public class Actividad {
    
    @Id
    @SequenceGenerator(
        name = "actividad_id_seq",
        sequenceName = "actividad_id_seq",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "actividad_id_seq"
    )
    private Integer id;
    
    @Column(name = "comuna_id")
    private Integer comunaId;
    
    @Column(length = 100)
    private String sector;
    
    @Column(nullable = false, length = 200)
    private String nombre;
    
    @Column(nullable = false, length = 100)
    private String email;
    
    @Column(length = 15)
    private String celular;
    
    @Column(name = "dia_hora_inicio", nullable = false)
    private LocalDateTime diaHoraInicio;
    
    @Column(name = "dia_hora_termino")
    private LocalDateTime diaHoraTermino;
    
    @Column(length = 255)
    private String descripcion;
    
    @ManyToOne
    @JoinColumn(name = "comuna_id", insertable = false, updatable = false)
    private Comuna comuna;
    
    @OneToMany(mappedBy = "actividad", cascade = CascadeType.ALL)
    private List<ActividadTema> temas;
    
    @OneToMany(mappedBy = "actividad", cascade = CascadeType.ALL)
    private List<ContactarPor> contactarPor;
    
    @OneToMany(mappedBy = "actividad", cascade = CascadeType.ALL)
    private List<Foto> fotos;
    
    @OneToMany(mappedBy = "actividad", cascade = CascadeType.ALL)
    private List<Comentario> comentarios;

    @OneToMany(mappedBy = "actividad", cascade = CascadeType.ALL)
    private List<Nota> notas;
    
    public Actividad(){}

    public Actividad( String nombre,
        String email,
        String celular,
        LocalDateTime diaHoraInicio,
        LocalDateTime diaHoraTermino,
        String descripcion) {
            this.nombre = nombre;
            this.email = email;
            this.celular = celular;
            this.diaHoraInicio = diaHoraInicio;
            this.diaHoraTermino = diaHoraTermino;
            this.descripcion = descripcion;
    }
    
    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Integer getComunaId() { return comunaId; }
    public void setComunaId(Integer comunaId) { this.comunaId = comunaId; }
    
    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }
    
    public LocalDateTime getDiaHoraInicio() { return diaHoraInicio; }
    public void setDiaHoraInicio(LocalDateTime diaHoraInicio) { this.diaHoraInicio = diaHoraInicio; }
    
    public LocalDateTime getDiaHoraTermino() { return diaHoraTermino; }
    public void setDiaHoraTermino(LocalDateTime diaHoraTermino) { this.diaHoraTermino = diaHoraTermino; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public Comuna getComuna() { return comuna; }
    public void setComuna(Comuna comuna) { this.comuna = comuna; }
    
    public List<ActividadTema> getTemas() { return temas; }
    public void setTemas(List<ActividadTema> temas) { this.temas = temas; }
    
    public List<ContactarPor> getContactarPor() { return contactarPor; }
    public void setContactarPor(List<ContactarPor> contactarPor) { this.contactarPor = contactarPor; }
    
    public List<Foto> getFotos() { return fotos; }
    public void setFotos(List<Foto> fotos) { this.fotos = fotos; }
    
    public List<Comentario> getComentarios() { return comentarios; }
    public void setComentarios(List<Comentario> comentarios) { this.comentarios = comentarios; }

    public List<Nota> getNotas() { return notas; }
    public void setNotas(List<Nota> notas) { this.notas = notas; }
}