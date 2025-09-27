package com.GestorDeCitas.Backend.controller;

import com.GestorDeCitas.Backend.DTOs.request.CitaRequest;
import com.GestorDeCitas.Backend.DTOs.response.CitaResponse;
import com.GestorDeCitas.Backend.Services.CitaService;
import com.GestorDeCitas.Backend.exceptions.CitaNotFoundException;
import com.GestorDeCitas.Backend.exceptions.HorarioNoDisponibleException;
import com.GestorDeCitas.Backend.models.Citas.EstadoCita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin (origins ={ "http://localhost:5173", "https://gestor-de-citas-dental-care.onrender.com"})
public class CitaController {
    @Autowired
    private CitaService citaService;


    //Crear nueva cita
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearCita(@Valid @RequestBody CitaRequest citaRequest) {
        try{

            CitaResponse citaCreada = citaService.crearCita(citaRequest);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cita agendada exitosamente");
            response.put("data", citaCreada);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);


        }catch(HorarioNoDisponibleException e){
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("error", "HORARIO_NO_DISPONIBLE");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);


        }catch (IllegalArgumentException e) {
            // Manejo para fechas pasadas u otros argumentos inválidos
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("error", "ARGUMENTO_INVALIDO");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse); // 400 Bad Request


        } catch (Exception e) {
            // Manejo general para otras excepciones
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor: " + e.getMessage());
            errorResponse.put("error", "INTERNAL_ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse); // 500 Internal Error
        }



    }

    // Obtener todas las citas
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodasLasCitas() {
        try {
            List<CitaResponse> citas = citaService.obtenerTodasLasCitas();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", citas);
            response.put("total", citas.size());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // Capturar errores específicos como "Usuario no encontrado"
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error de autenticación: " + e.getMessage());
            errorResponse.put("error", "AUTHENTICATION_ERROR");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

        } catch (Exception e) {
            // Log del error para debugging
            System.err.println("Error al obtener citas: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            errorResponse.put("error", "INTERNAL_SERVER_ERROR");
            errorResponse.put("details", e.getMessage()); // Solo para desarrollo

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    // Obtener cita por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerCitaPorId(@PathVariable Long id) {
        try {
            CitaResponse cita = citaService.obtenerCitaPorId(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", cita);

            return ResponseEntity.ok(response);

        } catch (CitaNotFoundException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("error", "CITA_NO_ENCONTRADA");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }


    // Obtener citas por fecha
    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<Map<String, Object>> obtenerCitasPorFecha(@PathVariable String fecha) {
        try {
            LocalDate fechaParsed = LocalDate.parse(fecha);
            List<CitaResponse> citas = citaService.obtenerCitasPorFecha(fechaParsed);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", citas);
            response.put("fecha", fecha);
            response.put("total", citas.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al obtener citas para la fecha especificada");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

    }

    // Obtener citas próximas
    @GetMapping("/proximas")
    public ResponseEntity<Map<String, Object>> obtenerCitasProximas() {
        try {
            List<CitaResponse> citas = citaService.obtenerCitasProximas();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", citas);
            response.put("total", citas.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al obtener citas próximas");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Actualizar estado de cita
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Map<String, Object>> actualizarEstadoCita(
            @PathVariable Long id,
            @RequestBody Map<String, String> estadoRequest) {
        try {
            EstadoCita nuevoEstado = EstadoCita.valueOf(estadoRequest.get("estado").toUpperCase());
            CitaResponse citaActualizada = citaService.actualizarEstadoCita(id, nuevoEstado);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Estado de cita actualizado exitosamente");
            response.put("data", citaActualizada);

            return ResponseEntity.ok(response);

        } catch (CitaNotFoundException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }


    // Cancelar cita
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Map<String, Object>> cancelarCita(@PathVariable Long id) {
        try {
            CitaResponse citaCancelada = citaService.cancelarCita(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cita cancelada exitosamente");
            response.put("data", citaCancelada);

            return ResponseEntity.ok(response);

        } catch (CitaNotFoundException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    // Eliminar cita permanentemente
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarCita(@PathVariable Long id) {
        try {
            citaService.eliminarCita(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cita eliminada exitosamente");

            return ResponseEntity.ok(response);

        } catch (CitaNotFoundException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }


}
