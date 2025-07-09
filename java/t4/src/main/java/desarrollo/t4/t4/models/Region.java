package desarrollo.t4.t4.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "region")
public class Region {
    
    private @Id
    @SequenceGenerator(
        name = "region_id_seq",
        sequenceName = "region_id_seq",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "region_id_seq"
    ) Long id;
    
    @Column(nullable = false, length = 200)
    private String nombre;
    
    @OneToMany(mappedBy = "region")
    private List<Comuna> comunas;
    
    // Constructors
    public Region() {}
    
    // Getters and Setters

    public Long getId() { return id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public List<Comuna> getComunas() { return comunas; }
    public void setComunas(List<Comuna> comunas) { this.comunas = comunas; }
}
