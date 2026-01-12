package com.tarea4.repository;

import com.tarea4.model.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad Nota.
 * Proporciona metodos para acceder a las notas/evaluaciones de actividades.
 */
@Repository
public interface NotaRepository extends JpaRepository<Nota, Long> {

    /**
     * Busca todas las notas de una actividad especifica.
     *
     * @param actividadId ID de la actividad
     * @return Lista de notas de la actividad
     */
    List<Nota> findByActividadId(Long actividadId);

    /**
     * Cuenta el numero de notas de una actividad.
     *
     * @param actividadId ID de la actividad
     * @return Numero de notas
     */
    Long countByActividadId(Long actividadId);

    /**
     * Calcula el promedio de notas de una actividad especifica.
     *
     * @param actividadId ID de la actividad
     * @return Promedio de notas o null si no hay notas
     */
    @Query("SELECT AVG(n.nota) FROM Nota n WHERE n.actividad.id = :actividadId")
    Double calcularPromedioPorActividad(@Param("actividadId") Long actividadId);

    /**
     * Obtiene la suma total de notas de una actividad.
     *
     * @param actividadId ID de la actividad
     * @return Suma de notas
     */
    @Query("SELECT SUM(n.nota) FROM Nota n WHERE n.actividad.id = :actividadId")
    Long sumNotasByActividadId(@Param("actividadId") Long actividadId);

    /**
     * Busca las ultimas N notas de una actividad.
     *
     * @param actividadId ID de la actividad
     * @return Lista de las ultimas notas ordenadas por fecha descendente
     */
    @Query("SELECT n FROM Nota n WHERE n.actividad.id = :actividadId ORDER BY n.fecha DESC")
    List<Nota> findUltimasNotasByActividadId(@Param("actividadId") Long actividadId);

    /**
     * Verifica si existe alguna nota para una actividad.
     *
     * @param actividadId ID de la actividad
     * @return true si existe al menos una nota
     */
    boolean existsByActividadId(Long actividadId);
}
