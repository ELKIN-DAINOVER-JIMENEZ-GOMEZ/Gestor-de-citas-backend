package com.GestorDeCitas.Backend.Services;

import com.GestorDeCitas.Backend.DTOs.request.CitaRequest;
import com.GestorDeCitas.Backend.DTOs.response.CitaResponse;
import com.GestorDeCitas.Backend.exceptions.CitaNotFoundException;
import com.GestorDeCitas.Backend.exceptions.HorarioNoDisponibleException;
import com.GestorDeCitas.Backend.models.Citas.Citas;
import com.GestorDeCitas.Backend.models.Citas.EstadoCita;
import com.GestorDeCitas.Backend.repository.CitaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class CitaService {

    private CitaRepository citaRepository;

    public CitaResponse crearCita(CitaRequest citaRequest) throws HorarioNoDisponibleException {
        // Validar que la fecha y hora estén disponibles
        if (citaRepository.existsByFechaAndHora(citaRequest.getFecha(), citaRequest.getHora())) {
            throw new HorarioNoDisponibleException("Ya existe una cita agendada para esa fecha y hora");
        }

        // Validar que la fecha no sea en el pasado
        if (citaRequest.getFecha().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se pueden agendar citas en fechas pasadas");
        }

        // Crear nueva cita
        Citas cita = new Citas();
        cita.setFecha(citaRequest.getFecha());
        cita.setHora(citaRequest.getHora());
        cita.setServicio(citaRequest.getServicio());
        cita.setNotas(citaRequest.getNotas());
        cita.setEstado(EstadoCita.PENDIENTE);

        // Guardar cita
        Citas citaGuardada = citaRepository.save(cita);

        // Convertir a DTO de respuesta
        return convertirAResponseDTO(citaGuardada);



    }

    // Obtener todas las citas
    public List<CitaResponse> obtenerTodasLasCitas() {
        return citaRepository.findAll()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    // Obtener citas por fecha
    public List<CitaResponse> obtenerCitasPorFecha(LocalDate fecha) {
        return citaRepository.findByFechaOrderByHora(fecha)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Obtener cita por ID
    public CitaResponse obtenerCitaPorId(Long id) throws CitaNotFoundException {
        Citas cita = citaRepository.findById(id)
                .orElseThrow(() -> new CitaNotFoundException("Cita no encontrada con ID: " + id));
        return convertirAResponseDTO(cita);
    }

    // Actualizar estado de cita
    public CitaResponse actualizarEstadoCita(Long id, EstadoCita nuevoEstado) throws CitaNotFoundException {
        Citas cita = citaRepository.findById(id)
                .orElseThrow(() -> new CitaNotFoundException("Cita no encontrada con ID: " + id));

        cita.setEstado(nuevoEstado);
        Citas citaActualizada = citaRepository.save(cita);

        return convertirAResponseDTO(citaActualizada);
    }

    // Cancelar cita
    public CitaResponse cancelarCita(Long id) throws CitaNotFoundException {
        return actualizarEstadoCita(id, EstadoCita.CANCELADA);
    }

    // Obtener citas próximas (siguientes 7 días)
    public List<CitaResponse> obtenerCitasProximas() {
        LocalDate hoy = LocalDate.now();
        LocalDate fechaLimite = hoy.plusDays(7);

        return citaRepository.findCitasProximas(hoy, fechaLimite)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Método auxiliar para convertir entidad a DTO
    private CitaResponse convertirAResponseDTO(Citas cita) {
        return new CitaResponse(
                cita.getId(),
                cita.getFecha(),
                cita.getHora(),
                cita.getServicio(),
                cita.getNotas(),
                cita.getEstado(),
                cita.getFechaCreacion()
        );
    }

}
