package com.GestorDeCitas.Backend.repository;

import com.GestorDeCitas.Backend.models.Citas.Citas;
import com.GestorDeCitas.Backend.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CitaRepository extends JpaRepository<Citas, Long> {

    // Métodos existentes modificados para incluir usuario
    @Query("SELECT c FROM Citas c WHERE c.usuario = :usuario AND c.fecha = :fecha ORDER BY c.hora ASC")
    List<Citas> findByUsuarioAndFechaOrderByHora(@Param("usuario") Users usuario, @Param("fecha") LocalDate fecha);

    boolean existsByFechaAndHora(LocalDate fecha, LocalTime hora);



    @Query("SELECT c FROM Citas c WHERE c.usuario = :usuario ORDER BY c.fecha ASC, c.hora ASC")
    List<Citas> findByUsuarioOrderByFechaAscHoraAsc(@Param("usuario") Users usuario);

    Optional<Citas> findByIdAndUsuario(Long id, Users usuario);

    // Buscar citas del usuario entre fechas
    @Query("SELECT c FROM Citas c WHERE c.usuario = :usuario AND c.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY c.fecha ASC, c.hora ASC")
    List<Citas> findByUsuarioAndFechaBetween(@Param("usuario") Users usuario,
                                             @Param("fechaInicio") LocalDate fechaInicio,
                                             @Param("fechaFin") LocalDate fechaFin);

    // Buscar citas próximas del usuario (siguientes 7 días)
    @Query("SELECT c FROM Citas c WHERE c.usuario = :usuario AND c.fecha >= :hoy AND c.fecha <= :fechaLimite AND c.estado != 'CANCELADA' ORDER BY c.fecha ASC, c.hora ASC")
    List<Citas> findCitasProximasByUsuario(@Param("usuario") Users usuario,
                                           @Param("hoy") LocalDate hoy,
                                           @Param("fechaLimite") LocalDate fechaLimite);
    // Verificar si existe cita en fecha/hora para cualquier usuario (para validación)
    boolean existsByFechaAndHoraAndUsuarioNot(LocalDate fecha, LocalTime hora, Users usuario);


    //Metodos para dar acceso a las citas a los admins
    // obtener todas las citas ordenadas:
    List<Citas> findAllByOrderByFechaAscHoraAsc();

    // Para buscar todas las citas entre fechas (para admin)
    @Query("SELECT c FROM Citas c WHERE c.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY c.fecha ASC, c.hora ASC")
    List<Citas> findAllByFechaBetween(@Param("fechaInicio") LocalDate fechaInicio,
                                      @Param("fechaFin") LocalDate fechaFin);
}
