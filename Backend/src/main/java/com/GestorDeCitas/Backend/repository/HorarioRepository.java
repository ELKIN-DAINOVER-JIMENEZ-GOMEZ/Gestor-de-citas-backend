package com.GestorDeCitas.Backend.repository;

import com.GestorDeCitas.Backend.models.Citas.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface HorarioRepository extends JpaRepository<Horario, Long> {


    List<Horario> findByFechaOrderByHora(LocalDate fecha);


    List<Horario> findByFechaAndDisponibleTrueOrderByHora(LocalDate fecha);


    boolean existsByFechaAndHora(LocalDate fecha, LocalTime hora);


    Optional<Horario> findByFechaAndHora(LocalDate fecha, LocalTime hora);


    @Query("SELECT h FROM Horario h WHERE h.fecha BETWEEN :fechaInicio AND :fechaFin AND h.disponible = true ORDER BY h.fecha, h.hora")
    List<Horario> findHorariosDisponiblesBetweenFechas(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );


    @Query("SELECT h FROM Horario h WHERE h.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY h.fecha, h.hora")
    List<Horario> findHorariosBetweenFechas(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );
}
