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
import org.springframework.web.multipart.MultipartFile;

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
            @RequestParam("telefono") String telefono,
            @RequestParam("contacto-tipo") List<String> contactoTipo,
            @RequestParam("contacto-identificador") List<String> contactoID,
            @RequestParam("fecha-y-hora-inicio") String fecha_inicio_str,
            @RequestParam("fecha-y-hora-fin") String fecha_fin_str,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("tema") String tema,
            @RequestParam("tema-otro") String temaOtro,
            @RequestParam("imagenes") List<MultipartFile> fotos,
            Model model
        ) {
        try {
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
