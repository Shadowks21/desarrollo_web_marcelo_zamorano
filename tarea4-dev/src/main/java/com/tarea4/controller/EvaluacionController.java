package com.tarea4.controller;

import com.tarea4.model.Actividad;
import com.tarea4.model.Nota;
import com.tarea4.service.EvaluacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador para el sistema de evaluacion de actividades.
 * Proporciona endpoints REST y vistas Thymeleaf.
 */
@Controller
public class EvaluacionController {

    private final EvaluacionService evaluacionService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Autowired
    public EvaluacionController(EvaluacionService evaluacionService) {
        this.evaluacionService = evaluacionService;
    }

    // ==================== VISTAS THYMELEAF ====================

    /**
     * Vista principal de evaluacion con listado de actividades realizadas.
     * GET /evaluacion
     *
     * @param model Modelo para la vista
     * @return Nombre del template Thymeleaf
     */
    @GetMapping("/evaluacion")
    public String vistaEvaluacion(Model model) {
        List<Actividad> actividades = evaluacionService.getActividadesRealizadas();

        // Preparar datos para la vista con promedios
        List<Map<String, Object>> actividadesConPromedio = actividades.stream()
                .map(act -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("id", act.getId());
                    data.put("fechaInicio", act.getFechaInicio().format(DATE_FORMATTER));
                    data.put("fechaTermino", act.getFechaTermino() != null ?
                            act.getFechaTermino().format(DATE_FORMATTER) : "N/A");
                    data.put("sector", act.getSector() != null ? act.getSector() : "Sin sector");
                    data.put("nombre", act.getNombre());
                    data.put("tema", act.getTemaDisplay());
                    data.put("comuna", act.getComuna() != null ? act.getComuna().getNombre() : "N/A");

                    Double promedio = evaluacionService.getPromedio(act.getId());
                    data.put("promedio", promedio != null ? promedio : "Sin evaluar");
                    data.put("numEvaluaciones", evaluacionService.contarEvaluaciones(act.getId()));

                    return data;
                })
                .collect(Collectors.toList());

        model.addAttribute("actividades", actividadesConPromedio);
        model.addAttribute("totalActividades", actividades.size());

        return "evaluacion";
    }

    /**
     * Pagina de inicio redirige a evaluacion.
     * GET /
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/evaluacion";
    }

    // ==================== API REST ====================

    /**
     * Obtiene lista de actividades realizadas en formato JSON.
     * GET /api/actividades
     *
     * @return Lista de actividades con sus datos
     */
    @GetMapping("/api/actividades")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getActividadesRealizadas() {
        List<Actividad> actividades = evaluacionService.getActividadesRealizadas();

        List<Map<String, Object>> resultado = actividades.stream()
                .map(act -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("id", act.getId());
                    data.put("fechaInicio", act.getFechaInicio().toString());
                    data.put("fechaTermino", act.getFechaTermino() != null ?
                            act.getFechaTermino().toString() : null);
                    data.put("sector", act.getSector());
                    data.put("nombre", act.getNombre());
                    data.put("tema", act.getTemaDisplay());
                    data.put("comuna", act.getComuna() != null ? act.getComuna().getNombre() : null);
                    data.put("descripcion", act.getDescripcion());

                    Double promedio = evaluacionService.getPromedio(act.getId());
                    data.put("promedio", promedio);
                    data.put("numEvaluaciones", evaluacionService.contarEvaluaciones(act.getId()));

                    return data;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(resultado);
    }

    /**
     * Obtiene los detalles de una actividad especifica.
     * GET /api/actividades/{id}
     *
     * @param id ID de la actividad
     * @return Datos de la actividad o 404 si no existe
     */
    @GetMapping("/api/actividades/{id}")
    @ResponseBody
    public ResponseEntity<?> getActividad(@PathVariable Long id) {
        return evaluacionService.getActividadById(id)
                .map(act -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("id", act.getId());
                    data.put("fechaInicio", act.getFechaInicio().toString());
                    data.put("fechaTermino", act.getFechaTermino() != null ?
                            act.getFechaTermino().toString() : null);
                    data.put("sector", act.getSector());
                    data.put("nombre", act.getNombre());
                    data.put("descripcion", act.getDescripcion());
                    data.put("promedio", evaluacionService.getPromedio(id));
                    data.put("numEvaluaciones", evaluacionService.contarEvaluaciones(id));
                    return ResponseEntity.ok(data);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Agrega una nueva nota/evaluacion a una actividad.
     * POST /api/actividades/{id}/nota
     * Body: {"nota": 1-7}
     *
     * @param id ID de la actividad
     * @param body Cuerpo con la nota
     * @return Nota creada o error
     */
    @PostMapping("/api/actividades/{id}/nota")
    @ResponseBody
    public ResponseEntity<?> agregarNota(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> body) {

        try {
            Integer valorNota = body.get("nota");

            if (valorNota == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El campo 'nota' es requerido");
                return ResponseEntity.badRequest().body(error);
            }

            Nota nota = evaluacionService.agregarNota(id, valorNota);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Nota agregada exitosamente");
            response.put("notaId", nota.getId());
            response.put("nota", nota.getNota());
            response.put("fecha", nota.getFecha().toString());
            response.put("nuevoPromedio", evaluacionService.getPromedio(id));
            response.put("totalEvaluaciones", evaluacionService.contarEvaluaciones(id));

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    /**
     * Obtiene el promedio de notas de una actividad.
     * GET /api/actividades/{id}/promedio
     *
     * @param id ID de la actividad
     * @return JSON con el promedio
     */
    @GetMapping("/api/actividades/{id}/promedio")
    @ResponseBody
    public ResponseEntity<?> getPromedio(@PathVariable Long id) {
        // Verificar que la actividad existe
        if (!evaluacionService.getActividadById(id).isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Actividad no encontrada");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        Double promedio = evaluacionService.getPromedio(id);
        Long numEvaluaciones = evaluacionService.contarEvaluaciones(id);

        Map<String, Object> response = new HashMap<>();
        response.put("actividadId", id);
        response.put("promedio", promedio != null ? promedio : 0.0);
        response.put("numEvaluaciones", numEvaluaciones);
        response.put("mensaje", promedio != null ?
                "Promedio calculado de " + numEvaluaciones + " evaluacion(es)" :
                "Sin evaluaciones aun");

        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene todas las notas de una actividad.
     * GET /api/actividades/{id}/notas
     *
     * @param id ID de la actividad
     * @return Lista de notas
     */
    @GetMapping("/api/actividades/{id}/notas")
    @ResponseBody
    public ResponseEntity<?> getNotas(@PathVariable Long id) {
        // Verificar que la actividad existe
        if (!evaluacionService.getActividadById(id).isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Actividad no encontrada");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        List<Nota> notas = evaluacionService.getNotasByActividadId(id);

        List<Map<String, Object>> resultado = notas.stream()
                .map(nota -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("id", nota.getId());
                    data.put("nota", nota.getNota());
                    data.put("fecha", nota.getFecha().toString());
                    return data;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(resultado);
    }
}
