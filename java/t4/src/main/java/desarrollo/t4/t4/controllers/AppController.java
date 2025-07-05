package desarrollo.t4.t4.controllers;

import desarrollo.t4.t4.models.Actividad;
import desarrollo.t4.t4.services.ApiService;
import desarrollo.t4.t4.services.AppService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/java")
public class AppController {

    private final AppService appService;
    private final ApiService apiService;

    public AppController(AppService appService, ApiService apiService) {
        this.appService = appService;
        this.apiService = apiService;
    }

    // Página principal - Home con actividades finalizadas
    @GetMapping("/")
    public String index(Model model) {
        // Obtener actividades finalizadas para el home
        List<Actividad> actividadesFinalizadas = apiService.getActividadesFinalizadas(10);
        model.addAttribute("actividades", actividadesFinalizadas);
        
        return "index";
    }

    // Página de listado de actividades
    @GetMapping("/actividades")
    public String listarActividades(Model model) {
        return "actividades";
    }

    // Página de detalle de actividad con puntuación
    @GetMapping("/actividad/{id}")
    public String detalleActividad(@PathVariable Integer id, Model model) {
        model.addAttribute("actividadId", id);
        return "detalle-actividad";
    }
}
