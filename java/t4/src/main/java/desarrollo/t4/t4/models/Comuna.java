package desarrollo.t4.t4.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "comuna")
public class Comuna {
    
    private @Id
    @SequenceGenerator(
        name = "comuna_id_seq",
        sequenceName = "comuna_id_seq",
        allocationSize = 1
    ) Long id;
    
    @Column(nullable = false, length = 200)
    private String nombre;
    
    @Column(name = "region_id", nullable = false)
    private Integer regionId;
    
    @ManyToOne
    @JoinColumn(name = "region_id", insertable = false, updatable = false)
    private Region region;
    
    @OneToMany(mappedBy = "comuna")
    private List<Actividad> actividades;
    
    // Constructors
    public Comuna() {}
    
    // Getters and Setters

    public Long getId() { return id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public Integer getRegionId() { return regionId; }
    public void setRegionId(Integer regionId) { this.regionId = regionId; }
    
    public Region getRegion() { return region; }
    public void setRegion(Region region) { this.region = region; }
    
    public List<Actividad> getActividades() { return actividades; }
    public void setActividades(List<Actividad> actividades) { this.actividades = actividades; }
}