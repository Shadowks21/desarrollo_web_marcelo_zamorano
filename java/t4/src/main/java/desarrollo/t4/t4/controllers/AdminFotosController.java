package desarrollo.t4.t4.controllers;

import desarrollo.t4.t4.models.Foto;
import desarrollo.t4.t4.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

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
    public String deleteFoto(
            Model model,
            @RequestParam("id") Long id,
            @RequestParam("message") String message) {
        try {
            adminService.deleteFoto(id, message);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/admin/admin-fotos?error";
        }
        return "redirect:/admin/admin-fotos";
    }

}
