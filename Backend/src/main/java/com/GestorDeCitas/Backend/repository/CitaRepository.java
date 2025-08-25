package com.GestorDeCitas.Backend.repository;

import com.GestorDeCitas.Backend.models.Citas.Citas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Repository
public interface CitaRepository extends JpaRepository<Citas, Long> {

    List<Citas> findByFechaOrderByHora(LocalDate fecha);

    boolean existsByFechaAndHora(LocalDate fecha, LocalTime hora);

    // Buscar citas entre fechas
    @Query("SELECT c FROM Citas c WHERE c.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY c.fecha ASC, c.hora ASC")
    List<Citas> findByFechaBetween(@Param("fechaInicio") LocalDate fechaInicio,
                                   @Param("fechaFin") LocalDate fechaFin);

    // Buscar citas próximas (siguientes 7 días)
    @Query("SELECT c FROM Citas c WHERE c.fecha >= :hoy AND c.fecha <= :fechaLimite AND c.estado != 'CANCELADA' ORDER BY c.fecha ASC, c.hora ASC")
    List<Citas> findCitasProximas(@Param("hoy") LocalDate hoy,
                                  @Param("fechaLimite") LocalDate fechaLimite);
}
