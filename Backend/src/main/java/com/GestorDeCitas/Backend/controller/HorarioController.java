package com.GestorDeCitas.Backend.controller;

import com.GestorDeCitas.Backend.DTOs.request.HorarioRequest;
import com.GestorDeCitas.Backend.DTOs.response.HorarioResponse;
import com.GestorDeCitas.Backend.Services.HorarioService;
import com.GestorDeCitas.Backend.exceptions.HorarioNotFoundException;
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
@RequestMapping("/api/horarios")
@CrossOrigin(origins = {"http://localhost:5173" , "https://gestor-de-citas-dental-care.onrender.com"})
public class HorarioController {
    @Autowired
    private HorarioService horarioService;


    //Crear nuevo horario
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearHorario(@Valid @RequestBody HorarioRequest horarioRequest) {
        try {
            HorarioResponse horarioCreado = horarioService.crearHorario(horarioRequest);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Horario creado exitosamente");
            response.put("data", horarioCreado);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al crear horario: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);

        }

    }

    // Obtener horarios disponibles por fecha (para pacientes)
    @GetMapping("/disponibles/{fecha}")
    public ResponseEntity<Map<String, Object>> obtenerHorariosDisponibles(@PathVariable String fecha) {
        try {
            LocalDate fechaParsed = LocalDate.parse(fecha);
            List<HorarioResponse> horarios = horarioService.obtenerHorariosDisponiblesPorFecha(fechaParsed);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", horarios);
            response.put("fecha", fecha);
            response.put("total", horarios.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al obtener horarios disponibles");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // Obtener todos los horarios por fecha (para administradores)
    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<Map<String, Object>> obtenerHorariosPorFecha(@PathVariable String fecha) {
        try {
            LocalDate fechaParsed = LocalDate.parse(fecha);
            List<HorarioResponse> horarios = horarioService.obtenerTodosLosHorariosPorFecha(fechaParsed);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", horarios);
            response.put("fecha", fecha);
            response.put("total", horarios.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al obtener horarios");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // Obtener todos los horarios
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodosLosHorarios() {
        try {
            List<HorarioResponse> horarios = horarioService.obtenerTodosLosHorarios();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", horarios);
            response.put("total", horarios.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al obtener horarios");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Actualizar disponibilidad de horario
    @PatchMapping("/{id}/disponibilidad")
    public ResponseEntity<Map<String, Object>> actualizarDisponibilidad(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> disponibilidadRequest) {
        try {
            Boolean nuevaDisponibilidad = disponibilidadRequest.get("disponible");
            HorarioResponse horarioActualizado = horarioService.actualizarDisponibilidad(id, nuevaDisponibilidad);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Disponibilidad actualizada exitosamente");
            response.put("data", horarioActualizado);

            return ResponseEntity.ok(response);

        } catch (HorarioNotFoundException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al actualizar disponibilidad");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Eliminar horario
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarHorario(@PathVariable Long id) {
        try {
            horarioService.eliminarHorario(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Horario eliminado exitosamente");

            return ResponseEntity.ok(response);

        } catch (HorarioNotFoundException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al eliminar horario");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

    }
}
