package desarrollo.t4.t4.services;

import desarrollo.t4.t4.models.Actividad;
import desarrollo.t4.t4.repositories.ActividadRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AppService {

    private final String pathStatic;
    private final ActividadRepository actividadRepository;

    public AppService(ActividadRepository actividadRepository) throws IOException {
        this.actividadRepository = actividadRepository;
        // Resolver dinámicamente la ruta absoluta para el directorio estático
        Path staticDir = Paths.get(ResourceUtils.getFile("classpath:static").getAbsolutePath());
        this.pathStatic = staticDir.toString();
        System.out.println("Ruta estática resuelta a: " + this.pathStatic);
    }
}