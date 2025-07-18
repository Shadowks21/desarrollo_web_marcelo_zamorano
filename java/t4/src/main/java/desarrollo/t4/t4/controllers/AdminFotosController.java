package desarrollo.t4.t4.controllers;

import desarrollo.t4.t4.models.Foto;
import desarrollo.t4.t4.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminFotosController {

    @Autowired
    private AdminService adminService;

    public AdminFotosController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admin-fotos")
    public String adminFotos(Model model) {
        List<Foto> fotos = adminService.findAllFotos();
        model.addAttribute("fotos", fotos);
        return "admin-fotos";
    }

    @PostMapping("/deleteFoto")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteFoto(
            @RequestParam("id") Long id,
            @RequestParam("message") String message) {
        Map<String, Object> response = new HashMap<>();
        try {
            adminService.deleteFoto(id, message);

            List<Foto> fotosRestantes = adminService.findAllFotos();
            response.put("success", true);
            response.put("message", "Foto eliminada correctamente");
            response.put("totalFotos", fotosRestantes.size());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

}
