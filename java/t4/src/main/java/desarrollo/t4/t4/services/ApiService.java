package desarrollo.t4.t4.services;

import desarrollo.t4.t4.models.Actividad;
import desarrollo.t4.t4.models.Nota;
import desarrollo.t4.t4.repositories.ActividadRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ApiService {
    private final ActividadRepository actividadRepository;
    private final Nota.NotaRepository notaRepository;

    public ApiService(ActividadRepository actividadRepository, Nota.NotaRepository notaRepository) {
        this.actividadRepository = actividadRepository;
        this.notaRepository = notaRepository;
    }
}
