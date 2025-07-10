package desarrollo.t4.t4.services;

import desarrollo.t4.t4.models.Actividad;
import desarrollo.t4.t4.models.Nota;
import desarrollo.t4.t4.repositories.ActividadRepository;
import desarrollo.t4.t4.repositories.NotaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ApiService {

    private final ActividadRepository actividadRepository;
    private final NotaRepository notaRepository;

    public ApiService(ActividadRepository actividadRepository, NotaRepository notaRepository) {
        this.actividadRepository = actividadRepository;
        this.notaRepository = notaRepository;
    }

    @Transactional
    public double agregarValoracion(Long actividadId, Integer nota) {
        // Verificar que la actividad existe
        Optional<Actividad> actividadOpt = actividadRepository.findById(actividadId);
        if (actividadOpt.isEmpty()) {
            throw new IllegalArgumentException("La actividad con ID " + actividadId + " no existe");
        }
        Actividad actividad = actividadOpt.get();
        // Crear nueva nota
        Nota nuevaNota = new Nota(actividad, nota);

        // Guardar la nota
        notaRepository.save(nuevaNota);

        // Calcular y retornar el promedio actualizado
        return calcularNotaPromedio(actividadId);
    }

    private double calcularNotaPromedio(Long actividadId) {
        List<Nota> notas = notaRepository.findAll().stream()
                .filter(nota -> nota.getActividad().getId().equals(actividadId))
                .toList();
        if (notas.isEmpty()) {
            return 0.0;
        }

        double suma = notas.stream()
                .mapToDouble(Nota::getNota)
                .sum();

        return suma / notas.size();
    }
}
