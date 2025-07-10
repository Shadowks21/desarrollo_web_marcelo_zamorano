package desarrollo.t4.t4.services;

import desarrollo.t4.t4.models.*;
import desarrollo.t4.t4.repositories.ActividadRepository;
import desarrollo.t4.t4.repositories.NotaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AppService {

    private final String pathStatic;

    private final ActividadRepository activityRepository;

    private final NotaRepository notaRepository;

    public AppService(ActividadRepository activityRepository, NotaRepository notaRepository) throws IOException {
        this.activityRepository = activityRepository;
        this.notaRepository = notaRepository;
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
            activityData.put("comuna", activity.getComuna().getNombre());
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
                if (tema.getTema() == "otro" | tema.getTema() == "Otro") {
                    ftema = tema.getGlosaOtro();
                }
                ftema = ftema.substring(0, 1).toUpperCase() + ftema.substring(1).toLowerCase();
                joinedTemas.append(ftema).append(", ");
            }
            activityData.put("temas", (joinedTemas.isEmpty()) ? "N/A" : joinedTemas.substring(0, joinedTemas.length() - 2));
            StringBuilder joinedContactarPor = new StringBuilder();

            // ContactarPor handling
            for (ContactarPor contacto : activity.getContactarPor()) {
                joinedContactarPor.append(contacto.getNombre()).append(", ");
            }
            activityData.put("contactarPor", joinedContactarPor.isEmpty() ? "N/A" : joinedContactarPor.substring(0, joinedContactarPor.length() - 2));
            StringBuilder joinedFotos = new StringBuilder();

            // Fotos handling
            for (Foto foto : activity.getFotos()) {
                joinedFotos.append(foto.getRutaArchivo()).append(", ");
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
        return getData(activities);
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
}
