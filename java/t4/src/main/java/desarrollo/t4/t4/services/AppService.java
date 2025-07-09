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

        for (Actividad activity : activities) {
            Map<String, String> activityData = new HashMap<>();
            activityData.put("id", activity.getId().toString());
            activityData.put("nombre", activity.getNombre());
            activityData.put("sector", activity.getSector());
            activityData.put("comuna", activity.getComuna().getNombre());
            activityData.put("region", activity.getComuna().getRegion().getNombre());
            activityData.put("email", activity.getEmail());
            activityData.put("celular", activity.getCelular());
            activityData.put("diaHoraInicio", activity.getDiaHoraInicio().toString());
            activityData.put("diaHoraTermino", activity.getDiaHoraTermino() != null ? activity.getDiaHoraTermino().toString() : "N/A");
            activityData.put("descripcion", activity.getDescripcion() != null ? activity.getDescripcion() : "N/A");
            StringBuilder joinedTemas = new StringBuilder();

            // Temas handling
            for (ActividadTema tema : activity.getTemas()) {
                joinedTemas.append(tema.getTema()).append(", ");
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
            StringBuilder joinedNotas = new StringBuilder();
            for (Nota nota : activity.getNotas()) {
                joinedNotas.append(nota.getNota()).append(", ");
            }
            activityData.put("notas", joinedNotas.isEmpty() ? "N/A" : joinedNotas.substring(0, joinedNotas.length() - 2));

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
        List<Actividad> finishedActivities = activityRepository.findAll().stream()
                .filter(activity -> activity.getDiaHoraTermino() != null && activity.getDiaHoraTermino().isBefore(ahora))
                .toList();

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
