package desarrollo.t4.t4.controllers;

import desarrollo.t4.t4.models.Actividad;
import desarrollo.t4.t4.services.ApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:5000", "http://127.0.0.1:5000"})
public class ApiController {

    private final ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    // GET /api/actividades/finalizadas - Obtener actividades finalizadas para el home
    @GetMapping("/actividades/finalizadas")
    public ResponseEntity<List<Actividad>> getActividadesFinalizadas(
            @RequestParam(defaultValue = "10") int limit) {
        List<Actividad> actividades = apiService.getActividadesFinalizadas(limit);
        return ResponseEntity.ok(actividades);
    }

    // GET /api/actividades - Listar todas las actividades con paginación
    @GetMapping("/actividades")
    public ResponseEntity<Map<String, Object>> listarActividades(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> resultado = apiService.listarActividades(page, size);
        return ResponseEntity.ok(resultado);
    }

    // POST /api/actividades/{id}/puntuar - Puntuar una actividad
    @PostMapping("/actividades/{id}/puntuar")
    public ResponseEntity<Map<String, Object>> puntuarActividad(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> puntuacion) {
        
        Integer puntaje = (Integer) puntuacion.get("puntaje");
        
        Map<String, Object> resultado = apiService.puntuarActividad(id, puntaje);
        return ResponseEntity.ok(resultado);
    }

    // GET /api/actividades/{id}/puntuaciones - Obtener puntuaciones de una actividad
    @GetMapping("/actividades/{id}/puntuaciones")
    public ResponseEntity<Map<String, Object>> getPuntuacionesActividad(@PathVariable Integer id) {
        Map<String, Object> puntuaciones = apiService.getPuntuacionesActividad(id);
        return ResponseEntity.ok(puntuaciones);
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> health = Map.of(
            "status", "OK",
            "service", "T4 API Service",
            "endpoints", "finalizadas, actividades, puntuar"
        );
        return ResponseEntity.ok(health);
    }
}