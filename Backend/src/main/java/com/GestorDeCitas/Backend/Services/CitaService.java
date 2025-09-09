package com.GestorDeCitas.Backend.Services;

import com.GestorDeCitas.Backend.DTOs.request.CitaRequest;
import com.GestorDeCitas.Backend.DTOs.response.CitaResponse;
import com.GestorDeCitas.Backend.DTOs.response.UserInfoResponse;
import com.GestorDeCitas.Backend.exceptions.CitaNotFoundException;
import com.GestorDeCitas.Backend.exceptions.HorarioNoDisponibleException;
import com.GestorDeCitas.Backend.models.Citas.Citas;
import com.GestorDeCitas.Backend.models.Citas.EstadoCita;
import com.GestorDeCitas.Backend.models.Users;
import com.GestorDeCitas.Backend.repository.CitaRepository;
import com.GestorDeCitas.Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class CitaService {
    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private UserRepository usuarioRepository;

    // Obtener usuario autenticado

    private Users getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // El token provee el username
        System.out.println("---[DEBUG]--- Buscando usuario con el principal (username): " + username);


        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con username: " + username));
    }


    public CitaResponse crearCita(CitaRequest citaRequest) throws HorarioNoDisponibleException {

        // Obtener el usuario autenticado
        Users usuario = getUsuarioAutenticado();

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
        cita.setUsuario(usuario);

        // Guardar cita
        Citas citaGuardada = citaRepository.save(cita);

        // Convertir a DTO de respuesta
        return convertirAResponseDTO(citaGuardada);



    }

    // Obtener todas las citas - Método mejorado
    public List<CitaResponse> obtenerTodasLasCitas() {
        try {
            Users usuario = getUsuarioAutenticado();

            List<Citas> citas = citaRepository.findByUsuarioOrderByFechaAscHoraAsc(usuario);

            // Verificar si la lista es null (aunque no debería ser con JPA)
            if (citas == null) {
                return new ArrayList<>();
            }

            return citas.stream()
                    .map(this::convertirAResponseDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            // Log el error específico
            System.err.println("Error en obtenerTodasLasCitas: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-lanzar para que el controller lo maneje
        }
    }


    // Obtener citas por fecha DEL USUARIO AUTENTICADO
    public List<CitaResponse> obtenerCitasPorFecha(LocalDate fecha) {
        Users usuario = getUsuarioAutenticado();
        return citaRepository.findByUsuarioAndFechaOrderByHora(usuario, fecha)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Obtener cita por ID (solo si pertenece al usuario autenticado)
    public CitaResponse obtenerCitaPorId(Long id) throws CitaNotFoundException {
        Users usuario = getUsuarioAutenticado();
        Citas cita = citaRepository.findByIdAndUsuario(id, usuario)
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


    // Cancelar cita (solo si pertenece al usuario autenticado)
    public CitaResponse cancelarCita(Long id) throws CitaNotFoundException {
        return actualizarEstadoCita(id, EstadoCita.CANCELADA);
    }

    // Obtener citas próximas DEL USUARIO AUTENTICADO
    public List<CitaResponse> obtenerCitasProximas() {
        Users usuario = getUsuarioAutenticado();
        LocalDate hoy = LocalDate.now();
        LocalDate fechaLimite = hoy.plusDays(7);

        return citaRepository.findCitasProximasByUsuario(usuario, hoy, fechaLimite)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Eliminar cita permanentemente (solo si pertenece al usuario autenticado)
    public void eliminarCita(Long id) throws CitaNotFoundException {
        Users usuario = getUsuarioAutenticado();
        Citas cita = citaRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new CitaNotFoundException("Cita no encontrada con ID: " + id));

        citaRepository.deleteById(id);
    }


    // Método auxiliar para convertir entidad a DTO
    private CitaResponse convertirAResponseDTO(Citas cita) {
        // Creamos el DTO del usuario
        UserInfoResponse usuario = new UserInfoResponse(cita.getUsuario());
        return new CitaResponse(
                cita.getId(),
                cita.getFecha(),
                cita.getHora(),
                cita.getServicio(),
                cita.getNotas(),
                cita.getEstado(),
                cita.getFechaCreacion(),
                usuario
        );
    }

}
