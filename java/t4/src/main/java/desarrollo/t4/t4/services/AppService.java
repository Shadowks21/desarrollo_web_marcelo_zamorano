package desarrollo.t4.t4.services;

import desarrollo.t4.t4.models.*;
import desarrollo.t4.t4.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.StreamSupport.stream;

@Service
@Transactional
public class AppService {

    private final String pathStatic;

    private final ActividadRepository activityRepository;

    private final NotaRepository notaRepository;

    private final ComunaRepository comunaRepository;

    private final ActividadTemaRepository actividadTemaRepository;

    private final ComentarioRepository comentarioRepository;

    private final ContactarPorRepository contactarPorRepository;

    private final FotoRepository fotoRepository;

    public AppService(ActividadRepository activityRepository,
                      ActividadTemaRepository actividadTemaRepository,
                      ComentarioRepository comentarioRepository,
                      ContactarPorRepository contactarPorRepository,
                      FotoRepository fotoRepository,
                      NotaRepository notaRepository,
                      ComunaRepository comunaRepository) throws IOException {

        this.activityRepository = activityRepository;
        this.notaRepository = notaRepository;
        this.comunaRepository = comunaRepository;
        this.actividadTemaRepository = actividadTemaRepository;
        this.comentarioRepository = comentarioRepository;
        this.contactarPorRepository = contactarPorRepository;
        this.fotoRepository = fotoRepository;
        Path staticDir = Paths.get(ResourceUtils.getFile("classpath:static").getAbsolutePath());
        this.pathStatic = staticDir.toString();
        System.out.println("Static path resolved to: " + this.pathStatic);
    }

    public List<Map<String, String>> getData(List<Actividad> activities) {
        List<Map<String, String>> activitiesData = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Actividad activity : activities) {
            Map<String, String> activityData = new HashMap<>();
            activityData.put("id", activity.getId().toString());
            activityData.put("nombre", activity.getNombre());
            activityData.put("sector", activity.getSector());

            if (activity.getComuna() != null) {
                activityData.put("comuna", activity.getComuna().getNombre());
            } else {
                activityData.put("comuna", "N/A");
            }

            activityData.put("region", activity.getComuna().getRegion().getNombre());
            activityData.put("email", activity.getEmail());
            activityData.put("celular", activity.getCelular());

            // Date and time handling
            activityData.put("diaHoraInicio", activity.getDiaHoraInicio().format(formatter));
            activityData.put("diaHoraTermino", activity.getDiaHoraTermino() != null ?
                    activity.getDiaHoraTermino().format(formatter) : "N/A");

            activityData.put("descripcion", activity.getDescripcion() != null ? activity.getDescripcion() : "N/A");
            StringBuilder joinedTemas = new StringBuilder();

            // Temas handling
            for (ActividadTema tema : activity.getTemas()) {
                String ftema = tema.getTema();
                if (ftema != null && ftema.equalsIgnoreCase("otro")) {
                    ftema = tema.getGlosaOtro();
                }
                ftema = ftema.substring(0, 1).toUpperCase() + ftema.substring(1).toLowerCase();
                joinedTemas.append(ftema).append(", ");
            }
            activityData.put("temas", (joinedTemas.isEmpty()) ? "N/A" : joinedTemas.substring(0, joinedTemas.length() - 2));
            // En el método getData, reemplaza la sección de ContactarPor con:
            StringBuilder joinedContactarPor = new StringBuilder();

            // ContactarPor handling
            for (ContactarPor contacto : activity.getContactarPor()) {
                String tipoContacto = contacto.getNombre();
                String identificador = contacto.getIdentificador();
                if (tipoContacto.equalsIgnoreCase("instagram")) {
                    identificador = "@" + identificador;
                }

                // Capitalizar primera letra del tipo de contacto
                if (!tipoContacto.isEmpty()) {
                    tipoContacto = tipoContacto.substring(0, 1).toUpperCase() +
                            tipoContacto.substring(1).toLowerCase();
                }

                joinedContactarPor.append(tipoContacto).append(": ").append(identificador).append(", ");
            }

            activityData.put("contactarPor", joinedContactarPor.isEmpty() ? "N/A" :
                    joinedContactarPor.substring(0, joinedContactarPor.length() - 2));StringBuilder joinedFotos = new StringBuilder();

            // Fotos handling
            for (Foto foto : activity.getFotos()) {
                joinedFotos.append(foto.getNombreArchivo()).append(", ");
            }
            activityData.put("fotos", joinedFotos.isEmpty() ? "N/A" : joinedFotos.substring(0, joinedFotos.length() - 2));

            // Notas handling
            Integer notaSum = 0;
            for (Nota nota : activity.getNotas()) {
                notaSum += nota.getNota();
            }
            double notaPromedio = activity.getNotas().isEmpty() ? 0.0 : (double) notaSum / activity.getNotas().size();
            String notaPromedioStr = notaPromedio == 0.0 ? "-" : String.format(Locale.US, "%.1f", notaPromedio);
            activityData.put("nota", notaPromedioStr);

            // Add the activity data to the list
            activitiesData.add(activityData);
        }
        return activitiesData;
    }

    public List<Map<String, String>> getActivitiesData() {
        List<Actividad> activities = activityRepository.findAll();
        activities.sort(Comparator.comparing(Actividad::getId).reversed());
        return getData(activities);
    }

    public List<Map<String, Object>> getActividadesPorDia() {
        List<Actividad> actividades = activityRepository.findAll();
        Map<String, Integer> actividadesPorDia = new HashMap<>();

        for (Actividad actividad : actividades) {
            String fecha = actividad.getDiaHoraInicio().toLocalDate().toString();
            actividadesPorDia.put(fecha, actividadesPorDia.getOrDefault(fecha, 0) + 1);
        }

        return actividadesPorDia.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("fecha", entry.getKey());
                    result.put("total", entry.getValue());
                    return result;
                })
                .sorted(Comparator.comparing(entry -> entry.get("fecha").toString()))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getActividadesPorTipo() {
        List<ActividadTema> actividadTemas = actividadTemaRepository.findAll();
        Map<String, Integer> actividadesPorTipo = new HashMap<>();

        for (ActividadTema actividadTema : actividadTemas) {
            String tema = actividadTema.getTema();
            if (tema.equals("otro") || tema.equals("Otro")) {
                tema = actividadTema.getGlosaOtro();
            }
            actividadesPorTipo.put(tema, actividadesPorTipo.getOrDefault(tema, 0) + 1);
        }

        return actividadesPorTipo.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("tema", entry.getKey());
                    result.put("total", entry.getValue());
                    return result;
                })
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getActividadesPorHorario() {
        List<Actividad> actividades = activityRepository.findAll();
        Map<String, int[]> actividadesPorMes = new HashMap<>();

        for (Actividad actividad : actividades) {
            String mes = actividad.getDiaHoraInicio().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            int hora = actividad.getDiaHoraInicio().getHour();

            actividadesPorMes.putIfAbsent(mes, new int[3]); // [mañana, mediodía, tarde]

            if (hora < 12) {
                actividadesPorMes.get(mes)[0]++; // mañana
            } else if (hora >= 12 && hora <= 17) {
                actividadesPorMes.get(mes)[1]++; // mediodía
            } else {
                actividadesPorMes.get(mes)[2]++; // tarde
            }
        }

        return actividadesPorMes.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("mes", entry.getKey());
                    result.put("manana", entry.getValue()[0]);
                    result.put("mediodia", entry.getValue()[1]);
                    result.put("tarde", entry.getValue()[2]);
                    return result;
                })
                .collect(Collectors.toList());
    }

    public List<Map<String, String>> getFinishedActivitiesData() {
        LocalDateTime ahora = LocalDateTime.now();
        List<Actividad> finishedActivities = new ArrayList<>(activityRepository.findAll().stream()
                .filter(activity -> activity.getDiaHoraInicio().isBefore(ahora))
                .toList());
        try {
            finishedActivities.sort(Comparator.comparing(Actividad::getId));
        } catch (Exception e) {
            System.err.println("Error sorting finished activities: " + e.getMessage());
        }
        return getData(finishedActivities);
    }

    public Map<String, Object> validateForm(
            String region, String comuna, String sector, String nombre, String email, String telefono,
            Map<String, String> contactos, LocalDateTime fechaInicio, LocalDateTime fechaFin,
            String descripcion, String ftema, List<MultipartFile> fotos) {

        Map<String, Object> validation = new HashMap<>();
        List<String> errors = new ArrayList<>();
        // Validar región
        if (region == null || region.trim().isEmpty()) {
            errors.add("Seleccione una región");
        }

        // Validar comuna
        if (comuna == null || comuna.trim().isEmpty()) {
            errors.add("Seleccione una comuna");
        }

        // Validar sector (opcional pero con formato)
        if (sector != null && !sector.trim().isEmpty()) {
            if (!sector.matches("^[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ\\s.,()-]+$")) {
                errors.add("El sector contiene caracteres no válidos");
            }
        }

        // Validar nombre (obligatorio)
        if (nombre == null || nombre.trim().isEmpty()) {
            errors.add("El nombre es obligatorio");
        } else if (!nombre.matches("^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+$")) {
            errors.add("El nombre solo puede contener letras y espacios");
        }

        // Validar email
        if (email == null || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            errors.add("Ingrese un email válido");
        }

        // Validar teléfono (opcional pero con formato +XXX.XXXXXXXX)
        if (telefono != null && !telefono.trim().isEmpty()) {
            if (!telefono.matches("^\\+\\d{1,3}\\.\\d{8,}$")) {
                errors.add("Formato de teléfono: +XXX.XXXXXXXX");
            }
        }

        // Validar fecha de inicio
        if (fechaInicio == null) {
            errors.add("La fecha de inicio es obligatoria");
        } else if (fechaInicio.isBefore(LocalDateTime.now())) {
            errors.add("La fecha debe ser futura");
        }

        // Validar fecha de fin (opcional)
        if (fechaFin != null && fechaInicio != null) {
            if (fechaFin.isBefore(fechaInicio)) {
                errors.add("La fecha de fin debe ser posterior al inicio");
            }
        }

        // Validar descripción (opcional pero con formato)
        if (descripcion != null && !descripcion.trim().isEmpty()) {
            if (!descripcion.matches("^[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ\\s.,!?¿¡()\"\\-]+$")) {
                errors.add("La descripción contiene caracteres no válidos");
            }
        }

        // Validar tema
        if (ftema == null || ftema.trim().isEmpty()) {
            errors.add("Seleccione un tema");
        } else {
            if (!ftema.matches("^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+$")) {
                errors.add("El tema especificado solo puede contener letras y espacios");
            }
        }

        // Validar fotos
        if (fotos == null || fotos.isEmpty()) {
            errors.add("Seleccione al menos una imagen");
        } else {
            if (fotos.size() > 5) {
                errors.add("Máximo 5 imágenes permitidas");
            }

            String[] validExtensions = {"jpg", "jpeg", "png"};
            long maxSize = 5 * 1024 * 1024; // 5MB

            for (MultipartFile foto : fotos) {
                if (foto.isEmpty()) continue;

                String filename = foto.getOriginalFilename();
                if (filename != null) {
                    String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
                    boolean validExtension = Arrays.asList(validExtensions).contains(extension);

                    if (!validExtension) {
                        errors.add("Solo se permiten archivos JPG, JPEG y PNG");
                        break;
                    }

                    if (foto.getSize() > maxSize) {
                        errors.add("El tamaño máximo por imagen es 5MB");
                        break;
                    }
                }
            }
        }

        // Preparar resultado
        validation.put("isValid", errors.isEmpty());
        validation.put("errors", errors);
        validation.put("errorMessage", errors.isEmpty() ? null : String.join(", ", errors));

        return validation;
    }

    public void handleAddActivity(String region, String comuna, String sector, String nombre, String email,
                                   String telefono, List<String> contactoTipo, List<String> contactoID, String fecha_inicio_str,
                                   String fecha_fin_str, String descripcion, String tema, String temaOtro,
                                   List<MultipartFile> fotos) throws Exception {

        try {
            Map<String, String> contactos = new HashMap<>();
            if (contactoTipo != null && contactoID != null && !contactoTipo.isEmpty() && !contactoID.isEmpty()) {
                for (int i = 0; i < contactoTipo.size(); i++) {
                    contactos.put(contactoTipo.get(i), contactoID.get(i));
                }
            }
            LocalDateTime fechaInicio = LocalDateTime.parse(fecha_inicio_str);
            LocalDateTime fechaFin = fecha_fin_str != null && !fecha_fin_str.isEmpty() ?
                    LocalDateTime.parse(fecha_fin_str) : null;
            String ftema = !Objects.equals(tema, "otro") ? tema : temaOtro;

            Map<String, Object> validation = validateForm(
                    region, comuna, sector, nombre, email, telefono,
                    contactos, fechaInicio, fechaFin,
                    descripcion, ftema, fotos
            );

            if (!(boolean) validation.get("isValid")) {
                throw new Exception((String) validation.get("errorMessage"));
            }
            Comuna comunaEntity = comunaRepository.findById(Long.parseLong(comuna))
                    .orElseThrow(() -> new Exception("Comuna ID not found: " + comuna));
            // 1. Crear la Actividad principal
            Actividad actividad = new Actividad();
            actividad.setNombre(nombre);
            actividad.setEmail(email);
            actividad.setCelular(telefono);
            actividad.setSector(sector);
            actividad.setDiaHoraInicio(fechaInicio);
            actividad.setDiaHoraTermino(fechaFin);
            actividad.setDescripcion(descripcion);
            actividad.setComuna(comunaEntity);
            actividad.setComunaId(comunaEntity.getId());
            actividad = activityRepository.save(actividad);
            activityRepository.flush(); // Ensure the activity is saved before proceeding

            // 2. Crear y asociar las fotos
            List<Foto> fotosList = new ArrayList<>();
            for (MultipartFile foto : fotos) {
                String _originalFilename = foto.getOriginalFilename();
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(_originalFilename.getBytes("UTF-8"));
                byte[] hash = md.digest();
                String _filename;
                try (Formatter formatter = new Formatter()) {
                    for (byte b : hash) {
                        formatter.format("%02x", b);
                    }
                    _filename = formatter.toString();
                }

                String _extension = _originalFilename.substring(_originalFilename.lastIndexOf('.') + 1).toLowerCase();
                if (!_extension.matches("jpg|jpeg|png|gif")) {
                    throw new IllegalArgumentException("Invalid file extension: " + _extension);
                }

                String imgFilename = _filename + "." + _extension;
                String relativePathImg = "/uploads/" + imgFilename;
                String finalPath = pathStatic + relativePathImg;

                System.out.println("Final image path: " + finalPath);

                // Ensure the uploads directory exists
                Path directoryPath = Paths.get(pathStatic + "/uploads");
                if (!Files.exists(directoryPath)) {
                    Files.createDirectories(directoryPath);
                    System.out.println("Uploads directory created.");
                }

                // Save the image file
                Path path = Paths.get(finalPath);
                try (InputStream inputStream = foto.getInputStream()) {
                    Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("File successfully saved at: " + path.toAbsolutePath());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save the image file.", e);
                }

                Foto fotoEntity = new Foto();
                fotoEntity.setRutaArchivo(relativePathImg);
                fotoEntity.setNombreArchivo(imgFilename);
                fotoEntity.setActividad(actividad);
                fotoEntity.setActividadId(actividad.getId());
                fotosList.add(fotoEntity);
            }
            fotoRepository.saveAll(fotosList);
            actividad.setFotos(fotosList);

            // 3. Crear y asociar los temas
            List<ActividadTema> temasList = new ArrayList<>();
            ActividadTema actividadTema = new ActividadTema();
            actividadTema.setTema(tema);
            if (tema.equals("otro")) {
                actividadTema.setGlosaOtro(temaOtro);
            }
            actividadTema.setActividad(actividad);
            actividadTema.setActividadId(actividad.getId());
            actividadTemaRepository.save(actividadTema);
            temasList.add(actividadTema);
            actividad.setTemas(temasList);

            // 4. Crear y asociar los contactos
            List<ContactarPor> contactosList = new ArrayList<>();
            for (Map.Entry<String, String> contacto : contactos.entrySet()) {
                ContactarPor contactarPor = new ContactarPor();
                contactarPor.setNombre(contacto.getKey());
                contactarPor.setIdentificador(contacto.getValue());
                contactarPor.setActividad(actividad);
                contactarPor.setActividadId(actividad.getId());
                contactosList.add(contactarPor);
            }
            contactarPorRepository.saveAll(contactosList);
            actividad.setContactarPor(contactosList);


        } catch (Exception e) {
            throw new Exception("Error while adding activity: " + e.getMessage(), e);
        }
    }

    public void handleRateActivity(Long actividadId, Integer rating) throws Exception {
        try {
            Actividad actividad = activityRepository.findById(actividadId)
                    .orElseThrow(() -> new Exception("Activity not found with ID: " + actividadId));

            Nota nota = new Nota();
            nota.setActividadId(actividadId);
            nota.setNota(rating);
            List<Nota> notas = actividad.getNotas();
            notas.add(nota);
            actividad.setNotas(notas);

            // Validate the rating
            if (rating < 1 || rating > 7) {
                throw new Exception("Rating must be between 1 and 7.");
            }

            // Save the updated activity and score
            notaRepository.save(nota);
            activityRepository.save(actividad);
        } catch (Exception e) {
            throw new Exception("Error while rating activity: " + e.getMessage(), e);
        }
    }

    public void agregarComentario(String nombre, String texto, Long actividadId) throws Exception {
        try {
            Comentario comentario = new Comentario(nombre, texto, actividadId);
            comentarioRepository.save(comentario);

        } catch (Exception e) {
            throw new Exception("Error al guardar comentario: " + e.getMessage());
        }
    }

    public List<Map<String, Object>> obtenerComentariosPorActividad(Long actividadId) {
        Actividad actividad = activityRepository.findById(actividadId).orElse(null);
        if (actividad == null) {
            return Collections.emptyList();
        }
        List<Comentario> comentarios = comentarioRepository.findAll();
        comentarios = comentarios.stream()
                .filter(comentario -> comentario.getActividadId().equals(actividadId))
                .sorted(Comparator.comparing(Comentario::getFecha).reversed())
                .toList();

        return comentarios.stream().map(comentario -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", comentario.getId());
            map.put("nombre", comentario.getNombre());
            map.put("texto", comentario.getTexto());
            map.put("fecha", comentario.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            return map;
        }).collect(Collectors.toList());
    }
}
