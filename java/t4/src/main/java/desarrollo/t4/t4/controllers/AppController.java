package desarrollo.t4.t4.controllers;

import desarrollo.t4.t4.models.Comuna;
import desarrollo.t4.t4.repositories.ComunaRepository;
import desarrollo.t4.t4.services.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class AppController {
    private final AppService appService;
    public AppController(AppService appService) {
        this.appService = appService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Map<String, String>> modelData = appService.getActivitiesData();
        model.addAttribute("activities", modelData);
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
    public String finishedActivity(Model model) {
        List<Map<String, String>> modelData = appService.getFinishedActivitiesData();
        model.addAttribute("activities", modelData);
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

    @Autowired
    private ComunaRepository comunaRepository;

    @GetMapping("/debug/comunas")
    public String debugComunas(Model model) {
        List<Comuna> comunas = comunaRepository.findAll();
        List<String> comunasNombres = comunas.stream().map(Comuna::getNombre).toList();
        model.addAttribute("comunas", comunasNombres);
        return "debug";
    }
}
