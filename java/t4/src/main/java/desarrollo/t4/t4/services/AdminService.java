package desarrollo.t4.t4.services;

import desarrollo.t4.t4.models.Foto;
import desarrollo.t4.t4.models.Log;
import desarrollo.t4.t4.repositories.AdminRepository;
import desarrollo.t4.t4.repositories.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private LogRepository logRepository;

    public AdminService(AdminRepository adminRepository, LogRepository logRepository) {
        this.adminRepository = adminRepository;
        this.logRepository = logRepository;
    }

    private String validarYLimpiarMensaje(String mensaje) {
        if (mensaje == null || mensaje.trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje es obligatorio");
        }

        // Limpiar el mensaje
        String mensajeLimpio = mensaje.trim()
                .replaceAll("<[^>]*>", "") // Remover tags HTML
                .replaceAll("[<>\"'&]", "") // Remover caracteres peligrosos
                .replaceAll("\\s+", " "); // Normalizar espacios

        // Validar longitud
        if (mensajeLimpio.length() < 5) {
            throw new IllegalArgumentException("El mensaje debe tener al menos 5 caracteres");
        }

        if (mensajeLimpio.length() > 200) {
            throw new IllegalArgumentException("El mensaje no puede exceder 200 caracteres");
        }

        // Validar contenido
        if (!mensajeLimpio.matches("^[a-zA-Z0-9\\sáéíóúÁÉÍÓÚñÑ.,;:()¡!¿?-]+$")) {
            throw new IllegalArgumentException("El mensaje contiene caracteres no permitidos");
        }

        return mensajeLimpio;
    }

    public List<Foto> findAllFotos() {
        return adminRepository.findAll();
    }

    public void deleteFoto(Long id, String message) throws RuntimeException {
        Foto foto = adminRepository.findById(id).orElseThrow(() -> new RuntimeException("Foto not found"));
        // Validate and clean the message
        try {
            message = validarYLimpiarMensaje(message);

            // Here you can add logic to handle the message if needed
            adminRepository.delete(foto);

            // Log the deletion
            logRepository.save(new Log("Eliminado foto: " + foto.getId() + " por usuario admin, motivo: " + message));

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error al eliminar la foto: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al eliminar la foto: " + e.getMessage(), e);
        }
    }

}
