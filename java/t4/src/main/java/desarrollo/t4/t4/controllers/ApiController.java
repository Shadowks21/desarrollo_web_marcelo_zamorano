package desarrollo.t4.t4.controllers;

import desarrollo.t4.t4.services.ApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ApiController {
    private final ApiService apiService;
    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @PostMapping("/valorar")
    public ResponseEntity<Map<String, Object>> valorarActividad(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            Long actividadId = Long.valueOf(request.get("actividadId").toString());
            int nota = Integer.parseInt(request.get("nota").toString());

            // Validar que la nota esté entre 1 y 7
            if (nota < 1 || nota > 7) {
                response.put("success", false);
                response.put("message", "La nota debe estar entre 1 y 7");
                return ResponseEntity.badRequest().body(response);
            }

            // Guardar la valoración y obtener la nueva nota promedio
            double notaPromedio = apiService.agregarValoracion(actividadId, nota);

            response.put("success", true);
            response.put("notaPromedio", notaPromedio);
            response.put("message", "Valoración guardada exitosamente");

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error interno del servidor");
            return ResponseEntity.status(500).body(response);
        }
    }
}
