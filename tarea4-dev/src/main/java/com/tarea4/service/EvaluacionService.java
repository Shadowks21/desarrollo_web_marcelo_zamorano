package com.tarea4.service;

import com.tarea4.model.Actividad;
import com.tarea4.model.Nota;
import com.tarea4.repository.ActividadRepository;
import com.tarea4.repository.NotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio que contiene la logica de negocio para la evaluacion de actividades.
 * Gestiona las operaciones relacionadas con notas y actividades realizadas.
 */
@Service
@Transactional
public class EvaluacionService {

    private final ActividadRepository actividadRepository;
    private final NotaRepository notaRepository;

    @Autowired
    public EvaluacionService(ActividadRepository actividadRepository, NotaRepository notaRepository) {
        this.actividadRepository = actividadRepository;
        this.notaRepository = notaRepository;
    }

    /**
     * Obtiene la lista de actividades que ya fueron realizadas.
     * Una actividad se considera realizada si su fecha_termino es anterior a la fecha actual.
     *
     * @return Lista de actividades realizadas con sus detalles
     */
    @Transactional(readOnly = true)
    public List<Actividad> getActividadesRealizadas() {
        LocalDateTime ahora = LocalDateTime.now();
        return actividadRepository.findActividadesRealizadasWithDetails(ahora);
    }

    /**
     * Obtiene una actividad por su ID.
     *
     * @param id ID de la actividad
     * @return Optional con la actividad o vacio si no existe
     */
    @Transactional(readOnly = true)
    public Optional<Actividad> getActividadById(Long id) {
        return actividadRepository.findById(id);
    }

    /**
     * Obtiene una actividad por su ID con sus temas cargados.
     *
     * @param id ID de la actividad
     * @return Actividad con temas o null si no existe
     */
    @Transactional(readOnly = true)
    public Actividad getActividadByIdWithTemas(Long id) {
        return actividadRepository.findByIdWithTemas(id);
    }

    /**
     * Agrega una nueva nota a una actividad.
     * Valida que la nota este en el rango 1-7 y que la actividad exista.
     *
     * @param actividadId ID de la actividad a evaluar
     * @param valorNota Valor de la nota (1-7)
     * @return La nota guardada
     * @throws IllegalArgumentException si la nota no esta en el rango valido
     * @throws RuntimeException si la actividad no existe
     */
    public Nota agregarNota(Long actividadId, Integer valorNota) {
        // Validar rango de nota
        if (valorNota == null || valorNota < 1 || valorNota > 7) {
            throw new IllegalArgumentException("La nota debe estar entre 1 y 7");
        }

        // Buscar la actividad
        Actividad actividad = actividadRepository.findById(actividadId)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada con ID: " + actividadId));

        // Crear y guardar la nota
        Nota nota = new Nota();
        nota.setNota(valorNota);
        nota.setActividad(actividad);
        nota.setFecha(LocalDateTime.now());

        return notaRepository.save(nota);
    }

    /**
     * Calcula el promedio de notas de una actividad.
     *
     * @param actividadId ID de la actividad
     * @return Promedio de notas redondeado a 1 decimal, o null si no hay notas
     */
    @Transactional(readOnly = true)
    public Double getPromedio(Long actividadId) {
        Double promedio = notaRepository.calcularPromedioPorActividad(actividadId);
        if (promedio != null) {
            // Redondear a 1 decimal
            return Math.round(promedio * 10.0) / 10.0;
        }
        return null;
    }

    /**
     * Obtiene todas las notas de una actividad.
     *
     * @param actividadId ID de la actividad
     * @return Lista de notas
     */
    @Transactional(readOnly = true)
    public List<Nota> getNotasByActividadId(Long actividadId) {
        return notaRepository.findByActividadId(actividadId);
    }

    /**
     * Cuenta el numero de evaluaciones de una actividad.
     *
     * @param actividadId ID de la actividad
     * @return Numero de evaluaciones
     */
    @Transactional(readOnly = true)
    public Long contarEvaluaciones(Long actividadId) {
        return notaRepository.countByActividadId(actividadId);
    }

    /**
     * Verifica si una actividad existe y esta realizada (terminada).
     *
     * @param actividadId ID de la actividad
     * @return true si la actividad existe y ya termino
     */
    @Transactional(readOnly = true)
    public boolean esActividadRealizada(Long actividadId) {
        Optional<Actividad> actividadOpt = actividadRepository.findById(actividadId);
        if (actividadOpt.isPresent()) {
            Actividad actividad = actividadOpt.get();
            return actividad.getFechaTermino() != null &&
                   actividad.getFechaTermino().isBefore(LocalDateTime.now());
        }
        return false;
    }

    /**
     * Cuenta el total de actividades realizadas.
     *
     * @return Numero total de actividades terminadas
     */
    @Transactional(readOnly = true)
    public Long contarActividadesRealizadas() {
        return actividadRepository.countActividadesRealizadas(LocalDateTime.now());
    }
}
