package desarrollo.t4.t4.controllers;

import desarrollo.t4.t4.models.Comuna;
import desarrollo.t4.t4.repositories.ComunaRepository;
import desarrollo.t4.t4.services.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class AppController {
    private final AppService appService;
    public AppController(AppService appService) {
        this.appService = appService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Map<String, String>> modelData = appService.getActivitiesData();
        model.addAttribute("activities", modelData.stream().limit(5).collect(Collectors.toList()));
        return "index";
    }

    @GetMapping("/agregarActividad")
    public String agregarActividad(Model model) {
        return "agregar-actividad";
    }

    @PostMapping("/agregar-actividad")
    public String agregarActividad(
            @RequestParam("select-region") String region,
            @RequestParam("select-comuna") String comuna,
            @RequestParam("sector") String sector,
            @RequestParam("nombre") String nombre,
            @RequestParam("email") String email,
            @RequestParam(value = "telefono", required = false) String telefono,
            @RequestParam(value = "contacto-tipo", required = false) List<String> contactoTipo,
            @RequestParam(value = "contacto-identificador", required = false) List<String> contactoID,
            @RequestParam("fecha-y-hora-inicio") String fecha_inicio_str,
            @RequestParam(value = "fecha-y-hora-fin", required = false) String fecha_fin_str,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam("tema") String tema,
            @RequestParam("tema-otro") String temaOtro,
            @RequestParam("imagenes") List<MultipartFile> fotos,
            Model model
        ) {
        try {
            if (contactoTipo == null) contactoTipo = new ArrayList<>();
            if (contactoID == null) contactoID = new ArrayList<>();

            appService.handleAddActivity(region, comuna, sector, nombre, email, telefono,
                    contactoTipo, contactoID, fecha_inicio_str, fecha_fin_str, descripcion, tema, temaOtro, fotos);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            List<String> errorMessages = List.of(errorMessage.split(","));
            model.addAttribute("errorMessages", errorMessages);
            return "agregar-actividad";
        }
        return "redirect:/";
    }

    @GetMapping("/listado-actividades")
    public String listadoActividades(
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        List<Map<String, String>> allActivities = appService.getActivitiesData();

        int pageSize = 5;
        int totalItems = allActivities.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);

        List<Map<String, String>> pageActivities = allActivities.subList(startIndex, endIndex);

        model.addAttribute("activities", pageActivities);
        model.addAttribute("page", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);

        return "listado-actividades";
    }

    @GetMapping("/detalle-actividad/{id}")
    public String detalleActividad(@PathVariable Long id, Model model) {
        try {
            List<Map<String, String>> allActivities = appService.getActivitiesData();
            Map<String, String> actividad = allActivities.stream()
                    .filter(a -> a.get("id").equals(id.toString()))
                    .findFirst()
                    .orElse(null);

            if (actividad == null) {
                return "redirect:/listado-actividades";
            }

            model.addAttribute("actividad", actividad);
            return "detalle-actividad";
        } catch (Exception e) {
            return "redirect:/listado-actividades";
        }
    }

    @PostMapping("/comentarios")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> agregarComentario(
            @RequestParam("nombre") String nombre,
            @RequestParam("texto") String texto,
            @RequestParam("actividadId") Long actividadId) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Validaciones
            if (nombre == null || nombre.trim().length() < 3 || nombre.trim().length() > 80) {
                response.put("success", false);
                response.put("error", "El nombre debe tener entre 3 y 80 caracteres");
                return ResponseEntity.badRequest().body(response);
            }

            if (texto == null || texto.trim().length() < 5) {
                response.put("success", false);
                response.put("error", "El comentario debe tener al menos 5 caracteres");
                return ResponseEntity.badRequest().body(response);
            }

            if (!nombre.matches("^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s.,!?¿¡()]+$")) {
                response.put("success", false);
                response.put("error", "El nombre contiene caracteres no permitidos");
                return ResponseEntity.badRequest().body(response);
            }

            appService.agregarComentario(nombre.trim(), texto.trim(), actividadId);

            response.put("success", true);
            response.put("message", "Comentario agregado exitosamente");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al agregar comentario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/comentarios/{actividadId}")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> obtenerComentarios(@PathVariable Long actividadId) {
        try {
            List<Map<String, Object>> comentarios = appService.obtenerComentariosPorActividad(actividadId);
            return ResponseEntity.ok(comentarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    @GetMapping("/stats")
    public String stats() {
        return "stats";
    }

    @GetMapping("/stats/actividades_por_dia")
    @ResponseBody
    public List<Map<String, Object>> getActividadesPorDia() {
        return appService.getActividadesPorDia();
    }

    @GetMapping("/stats/actividades_por_tipo")
    @ResponseBody
    public List<Map<String, Object>> getActividadesPorTipo() {
        return appService.getActividadesPorTipo();
    }

    @GetMapping("/stats/actividades_por_horario")
    @ResponseBody
    public List<Map<String, Object>> getActividadesPorHorario() {
        return appService.getActividadesPorHorario();
    }

    @GetMapping("/finishedActivity")
    public String finishedActivity(
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        List<Map<String, String>> allActivities = appService.getFinishedActivitiesData();

        int pageSize = 6; // Máximo 6 tarjetas por página
        int totalItems = allActivities.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);

        List<Map<String, String>> pageActivities = allActivities.subList(startIndex, endIndex);

        model.addAttribute("activities", pageActivities);
        model.addAttribute("page", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);

        return "listado-actividad";
    }

    @PostMapping("/rateActivity")
    public String rateActivity(
            @RequestParam("actividadId") Long actividadId,
            @RequestParam("rating") Integer rating
        ) throws Exception {
        appService.handleRateActivity(actividadId, rating);
        return "redirect:/finishedActivity";
    }
}
