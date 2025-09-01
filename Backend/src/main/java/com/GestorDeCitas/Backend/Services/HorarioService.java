package com.GestorDeCitas.Backend.Services;

import com.GestorDeCitas.Backend.DTOs.request.HorarioRequest;
import com.GestorDeCitas.Backend.DTOs.response.HorarioResponse;
import com.GestorDeCitas.Backend.exceptions.HorarioNotFoundException;
import com.GestorDeCitas.Backend.models.Citas.Horario;
import com.GestorDeCitas.Backend.repository.CitaRepository;
import com.GestorDeCitas.Backend.repository.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HorarioService {
    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private CitaRepository citaRepository;

    // Crear nuevo horario
    public HorarioResponse crearHorario(HorarioRequest horarioRequest) {
        // Verificar si ya existe un horario para esa fecha y hora
        Optional<Horario> horarioExistente = horarioRepository
                .findByFechaAndHora(horarioRequest.getFecha(), horarioRequest.getHora());

        if (horarioExistente.isPresent()) {
            // Si existe, actualizar su disponibilidad
            Horario horario = horarioExistente.get();
            horario.setDisponible(horarioRequest.getDisponible());
            Horario horarioActualizado = horarioRepository.save(horario);
            return convertirAResponseDTO(horarioActualizado);
        } else {
            // Si no existe, crear nuevo horario
            Horario nuevoHorario = new Horario();
            nuevoHorario.setFecha(horarioRequest.getFecha());
            nuevoHorario.setHora(horarioRequest.getHora());
            nuevoHorario.setDisponible(horarioRequest.getDisponible());

            Horario horarioGuardado = horarioRepository.save(nuevoHorario);
            return convertirAResponseDTO(horarioGuardado);
        }
    }

    // Obtener horarios disponibles por fecha
    public List<HorarioResponse> obtenerHorariosDisponiblesPorFecha(LocalDate fecha) {
        return horarioRepository.findByFechaAndDisponibleTrueOrderByHora(fecha)
                .stream()
                .filter(horario -> !tieneicitaAgendada(fecha, horario.getHora()))
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Obtener todos los horarios por fecha (para admin)
    public List<HorarioResponse> obtenerTodosLosHorariosPorFecha(LocalDate fecha) {
        return horarioRepository.findByFechaOrderByHora(fecha)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Obtener todos los horarios
    public List<HorarioResponse> obtenerTodosLosHorarios() {
        return horarioRepository.findAll()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Actualizar disponibilidad de horario
    public HorarioResponse actualizarDisponibilidad(Long id, Boolean disponible) throws HorarioNotFoundException {
        Horario horario = horarioRepository.findById(id)
                .orElseThrow(() -> new HorarioNotFoundException("Horario no encontrado con ID: " + id));

        horario.setDisponible(disponible);
        Horario horarioActualizado = horarioRepository.save(horario);

        return convertirAResponseDTO(horarioActualizado);
    }

    // Eliminar horario
    public void eliminarHorario(Long id) throws HorarioNotFoundException {
        if (!horarioRepository.existsById(id)) {
            throw new HorarioNotFoundException("Horario no encontrado con ID: " + id);
        }
        horarioRepository.deleteById(id);
    }

    // Verificar si un horario tiene cita agendada
    private boolean tieneCtaAgendada(LocalDate fecha, LocalTime hora) {
        return citaRepository.existsByFechaAndHora(fecha, hora);
    }

    // Obtener horarios disponibles en un rango de fechas
    public List<HorarioResponse> obtenerHorariosDisponiblesEnRango(LocalDate fechaInicio, LocalDate fechaFin) {
        return horarioRepository.findHorariosDisponiblesBetweenFechas(fechaInicio, fechaFin)
                .stream()
                .filter(horario -> !tieneicitaAgendada(horario.getFecha(), horario.getHora()))
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Método auxiliar para verificar si tiene cita agendada
    private boolean tieneicitaAgendada(LocalDate fecha, LocalTime hora) {
        return citaRepository.existsByFechaAndHora(fecha, hora);
    }

    // Convertir entidad a DTO
    private HorarioResponse convertirAResponseDTO(Horario horario) {
        return new HorarioResponse(
                horario.getId(),
                horario.getFecha(),
                horario.getHora(),
                horario.getDisponible(),
                horario.getFechaCreacion()

        );
    }

}
