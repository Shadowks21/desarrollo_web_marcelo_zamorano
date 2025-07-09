package desarrollo.t4.t4.controllers;

import desarrollo.t4.t4.models.Foto;
import desarrollo.t4.t4.services.AppService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/finishedActivity")
    public String finishedActivity(Model model) {
        List<Map<String, String>> modelData = appService.getFinishedActivitiesData();
        model.addAttribute("activities", modelData);
        return "finishedActivity";
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
