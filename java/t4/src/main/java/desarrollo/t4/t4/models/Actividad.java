package desarrollo.t4.t4.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

@Entity
@Table(name = "actividad")
public class Actividad {

    private @Id
    @SequenceGenerator(
            name = "actividad_id_seq",
            sequenceName = "actividad_id_seq",
            allocationSize = 1
    ) @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "actividad_id_seq"
    ) Long id;

    @Column(name = "comuna_id")
    private Long comunaId;

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

    public Actividad() {
    }

    public Actividad(
            String nombre,
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

    private static final Pattern ALPHANUMERIC_WITH_ACCENTS = Pattern.compile("^[\\w\\sáéíóúÁÉÍÓÚñÑ]{1,100}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[\\w\\sáéíóúÁÉÍÓÚñÑ]{1,200}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.+]+@[a-zA-Z_]+\\.[a-zA-Z]{2,3}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+\\d{3}\\.\\d{8}$");
    private static final Pattern DESCRIPTION_PATTERN = Pattern.compile("^[\\w\\sáéíóúÁÉÍÓÚñÑ¿?¡!.,;:()\"]{1,500}$");
    private static final Pattern CONTACT_ID_PATTERN = Pattern.compile("^[\\w\\sáéíóúÁÉÍÓÚñÑ]{4,50}$");
    private static final Pattern URL_PATTERN = Pattern.compile("^https?://[^\\s/$.?#].[^\\s]*$");

    private static final List<String> TEMAS_PERMITIDOS = Arrays.asList(
            "musica", "deporte", "ciencias", "religion", "politica",
            "tecnologia", "juegos", "baile", "comida", "otro"
    );

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public Long getComunaId() {
        return comunaId;
    }

    public void setComunaId(Long comunaId) {
        this.comunaId = comunaId;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public LocalDateTime getDiaHoraInicio() {
        return diaHoraInicio;
    }

    public void setDiaHoraInicio(LocalDateTime diaHoraInicio) {
        this.diaHoraInicio = diaHoraInicio;
    }

    public LocalDateTime getDiaHoraTermino() {
        return diaHoraTermino;
    }

    public void setDiaHoraTermino(LocalDateTime diaHoraTermino) {
        this.diaHoraTermino = diaHoraTermino;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Comuna getComuna() {
        return comuna;
    }

    public void setComuna(Comuna comuna) {
        this.comuna = comuna;
    }

    public List<ActividadTema> getTemas() {
        return temas;
    }

    public void setTemas(List<ActividadTema> temas) {
        this.temas = temas;
    }

    public List<ContactarPor> getContactarPor() {
        return contactarPor;
    }

    public void setContactarPor(List<ContactarPor> contactarPor) {
        this.contactarPor = contactarPor;
    }

    public List<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(List<Foto> fotos) {
        this.fotos = fotos;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public List<Nota> getNotas() {
        return notas;
    }

    public void setNotas(List<Nota> notas) {
        this.notas = notas;
    }

    // Validator
    public static Hashtable<String, Map<Boolean, String>> isValidActivity(Actividad actividad) {

        Hashtable<String, Map<Boolean, String>> errors = new Hashtable<>();

        if (actividad == null) {
            Map<Boolean, String> activityErrors = new HashMap<>();
            activityErrors.put(false, "La actividad no puede ser nula");
            errors.put("Activity", activityErrors);
            return errors;
        }

        // Validar comuna (región implícita)
        if (actividad.getComunaId() == null) {
            Map<Boolean, String> comunaErrors = new HashMap<>();
            comunaErrors.put(false, "La comuna es obligatoria");
            errors.put("Comuna", comunaErrors);

        }

        // Validar sector
        if (actividad.getSector() != null && !actividad.getSector().trim().isEmpty()) {
            String sector = actividad.getSector().trim();
            if (!ALPHANUMERIC_WITH_ACCENTS.matcher(sector).matches()) {
                Map<Boolean, String> sectorErrors = new HashMap<>();
                sectorErrors.put(false, "El sector debe contener máximo 100 caracteres alfanuméricos");
                errors.put("Sector", sectorErrors);
            }
        }

        // Validar nombre
        if (actividad.getNombre() == null || actividad.getNombre().trim().isEmpty()) {
            Map<Boolean, String> nameErrors = new HashMap<>();
            nameErrors.put(false, "El nombre es obligatorio");
            errors.put("Nombre", nameErrors);
        } else {
            String nombre = actividad.getNombre().trim();
            if (!NAME_PATTERN.matcher(nombre).matches()) {
                Map<Boolean, String> nameErrors = new HashMap<>();
                nameErrors.put(false, "El nombre debe contener máximo 200 caracteres alfanuméricos");
                errors.put("Nombre", nameErrors);
            }
        }

        // Validar email
        if (actividad.getEmail() == null || actividad.getEmail().trim().isEmpty()) {
            Map<Boolean, String> emailErrors = new HashMap<>();
            emailErrors.put(false, "El email es obligatorio");
            errors.put("Email", emailErrors);
        } else {
            String email = actividad.getEmail().trim();
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                Map<Boolean, String> emailErrors = new HashMap<>();
                emailErrors.put(false, "El formato del email es inválido");
                errors.put("Email", emailErrors);
            }
        }

        // Validar celular (opcional)
        if (actividad.getCelular() != null && !actividad.getCelular().trim().isEmpty()) {
            String celular = actividad.getCelular().trim();
            if (!PHONE_PATTERN.matcher(celular).matches()) {
                Map<Boolean, String> celularErrors = new HashMap<>();
                celularErrors.put(false, "El formato del celular debe ser +XXX.XXXXXXXX");
                errors.put("Celular", celularErrors);
            }
        }

        // Validar fecha de inicio
        if (actividad.getDiaHoraInicio() == null) {
            Map<Boolean, String> fechaInicioErrors = new HashMap<>();
            fechaInicioErrors.put(false, "La fecha de inicio es obligatoria");
            errors.put("FechaInicio", fechaInicioErrors);
        } else {
            LocalDateTime ahora = LocalDateTime.now();
            if (actividad.getDiaHoraInicio().isBefore(ahora)) {
                Map<Boolean, String> fechaInicioErrors = new HashMap<>();
                fechaInicioErrors.put(false, "La fecha de inicio debe ser futura");
                errors.put("FechaInicio", fechaInicioErrors);
            }
        }

        // Validar fecha de término
        if (actividad.getDiaHoraTermino() != null && actividad.getDiaHoraInicio() != null) {
            if (actividad.getDiaHoraTermino().isBefore(actividad.getDiaHoraInicio())) {
                Map<Boolean, String> fechaTerminoErrors = new HashMap<>();
                fechaTerminoErrors.put(false, "La fecha de término debe ser posterior a la fecha de inicio");
                errors.put("FechaTermino", fechaTerminoErrors);
            }
        }

        // Validar descripción (opcional)
        if (actividad.getDescripcion() != null && !actividad.getDescripcion().trim().isEmpty()) {
            String descripcion = actividad.getDescripcion().trim();
            if (!DESCRIPTION_PATTERN.matcher(descripcion).matches()) {
                Map<Boolean, String> descripcionErrors = new HashMap<>();
                descripcionErrors.put(false, "La descripción debe tener máximo 500 caracteres válidos");
                errors.put("Descripcion", descripcionErrors);
            }
        }

        // Validar contactar por
        if (actividad.getContactarPor() == null || actividad.getContactarPor().isEmpty()) {
            Map<Boolean, String> contactoErrors = new HashMap<>();
            contactoErrors.put(false, "Debe especificar al menos un medio de contacto");
            errors.put("ContactarPor", contactoErrors);
        } else {
            for (ContactarPor contacto : actividad.getContactarPor()) {
                if (contacto.getIdentificador() == null || contacto.getIdentificador().trim().isEmpty()) {
                    Map<Boolean, String> contactoErrors = new HashMap<>();
                    contactoErrors.put(false, "El identificador de contacto es obligatorio");
                    errors.put("ContactarPor", contactoErrors);
                    continue;
                }

                String identificador = contacto.getIdentificador().trim();
                boolean isValidContact = CONTACT_ID_PATTERN.matcher(identificador).matches();
                boolean isValidUrl = URL_PATTERN.matcher(identificador).matches() &&
                        identificador.length() >= 4 && identificador.length() <= 50;

                if (!isValidContact && !isValidUrl) {
                    Map<Boolean, String> contactoErrors = new HashMap<>();
                    contactoErrors.put(false, "El identificador de contacto debe tener entre 4 y 50 caracteres válidos");
                    errors.put("ContactarPor", contactoErrors);
                }
            }
        }

        // Validar temas
        if (actividad.getTemas() == null || actividad.getTemas().isEmpty()) {
            Map<Boolean, String> temasErrors = new HashMap<>();
            temasErrors.put(false, "Debe seleccionar al menos un tema");
            errors.put("Temas", temasErrors);
        } else {
            for (ActividadTema tema : actividad.getTemas()) {
                if (tema.getTema() == null || tema.getGlosaOtro() == null) {
                    Map<Boolean, String> temasErrors = new HashMap<>();
                    temasErrors.put(false, "Tema inválido");
                    errors.put("Temas", temasErrors);
                    continue;
                }

                String nombreTema = tema.getTema().toLowerCase();
                if (!TEMAS_PERMITIDOS.contains(nombreTema)) {
                    if (nombreTema.contains("-")) {
                        String temaBase = nombreTema.split("-")[0];
                        if (!Pattern.compile("^[\\w\\sáéíóúÁÉÍÓÚñÑ.]{1,500}$").matcher(temaBase).matches()) {
                            Map<Boolean, String> temasErrors = new HashMap<>();
                            temasErrors.put(false, "El tema especificado no es válido");
                            errors.put("Temas", temasErrors);
                        }
                    } else {
                        Map<Boolean, String> temasErrors = new HashMap<>();
                        temasErrors.put(false, "El tema debe estar entre los permitidos");
                        errors.put("Temas", temasErrors);
                    }
                }
            }
        }

        // Validar fotos
        if (actividad.getFotos() == null || actividad.getFotos().isEmpty()) {
            Map<Boolean, String> fotosErrors = new HashMap<>();
            fotosErrors.put(false, "Debe incluir al menos una foto");
            errors.put("Fotos", fotosErrors);
        }

        // Si no hay errores, la actividad es válida
        if (errors.isEmpty()) {
            Map<Boolean, String> validResult = new HashMap<>();
            validResult.put(true, "Actividad válida");
            errors.put("Valid", validResult);
        } else {
            Map<Boolean, String> invalidResult = new HashMap<>();
            invalidResult.put(false, "Actividad inválida");
            errors.put("Valid", invalidResult);
        }

        return errors;
    }
}