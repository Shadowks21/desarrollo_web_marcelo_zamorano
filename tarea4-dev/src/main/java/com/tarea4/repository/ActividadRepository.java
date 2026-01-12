package com.tarea4.repository;

import com.tarea4.model.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio JPA para la entidad Actividad.
 * Proporciona metodos para acceder a las actividades en la base de datos.
 */
@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {

    /**
     * Busca actividades que ya han terminado (fecha_termino < fecha actual).
     * Ordenadas por fecha de inicio descendente.
     *
     * @param fecha Fecha limite (generalmente LocalDateTime.now())
     * @return Lista de actividades terminadas
     */
    List<Actividad> findByFechaTerminoBeforeOrderByFechaInicioDesc(LocalDateTime fecha);

    /**
     * Busca actividades terminadas con fecha_termino no nula y anterior a la fecha dada.
     *
     * @param fecha Fecha limite
     * @return Lista de actividades realizadas
     */
    @Query("SELECT a FROM Actividad a WHERE a.fechaTermino IS NOT NULL AND a.fechaTermino < :fecha ORDER BY a.fechaInicio DESC")
    List<Actividad> findActividadesRealizadas(@Param("fecha") LocalDateTime fecha);

    /**
     * Cuenta el total de actividades terminadas.
     *
     * @param fecha Fecha limite
     * @return Numero de actividades terminadas
     */
    @Query("SELECT COUNT(a) FROM Actividad a WHERE a.fechaTermino IS NOT NULL AND a.fechaTermino < :fecha")
    Long countActividadesRealizadas(@Param("fecha") LocalDateTime fecha);

    /**
     * Busca una actividad por ID con sus temas cargados.
     *
     * @param id ID de la actividad
     * @return Actividad con temas
     */
    @Query("SELECT DISTINCT a FROM Actividad a LEFT JOIN FETCH a.temas WHERE a.id = :id")
    Actividad findByIdWithTemas(@Param("id") Long id);

    /**
     * Busca actividades realizadas con sus temas y comunas cargados.
     * Evita el problema N+1.
     *
     * @param fecha Fecha limite
     * @return Lista de actividades con relaciones cargadas
     */
    @Query("SELECT DISTINCT a FROM Actividad a " +
           "LEFT JOIN FETCH a.temas " +
           "LEFT JOIN FETCH a.comuna c " +
           "LEFT JOIN FETCH c.region " +
           "WHERE a.fechaTermino IS NOT NULL AND a.fechaTermino < :fecha " +
           "ORDER BY a.fechaInicio DESC")
    List<Actividad> findActividadesRealizadasWithDetails(@Param("fecha") LocalDateTime fecha);
}
