package com.GestorDeCitas.Backend.controller;

import com.GestorDeCitas.Backend.DTOs.request.CrearMensaje;
import com.GestorDeCitas.Backend.DTOs.response.EstadisticasMensajes;
import com.GestorDeCitas.Backend.DTOs.response.MensajeResponse;
import com.GestorDeCitas.Backend.Services.MensajeService;
import com.GestorDeCitas.Backend.models.Mensajes.TipoMensaje;
import com.GestorDeCitas.Backend.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/mensajes")
@CrossOrigin(origins = {"http://localhost:5173" , "https://gestor-de-citas-dental-care.onrender.com"})
public class MensajeController {
    @Autowired
    private MensajeService mensajeService;


    // Crear nuevo mensaje (cualquier usuario autenticado puede enviar)
    @PostMapping
    public ResponseEntity<ApiResponse<MensajeResponse>> crearMensaje(
            @Valid @RequestBody CrearMensaje crearMensajeDTO,
            Authentication authentication) {

        try {
            MensajeResponse mensaje = mensajeService.crearMensaje(crearMensajeDTO, authentication);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Mensaje enviado exitosamente", mensaje));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, "Error al enviar el mensaje: " + e.getMessage(), null));
        }
    }

    // Obtener mensajes del usuario autenticado
    @GetMapping("/mis-recibidos")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Page<MensajeResponse>>> obtenerMisMensajesRecibidos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String busqueda,
            Authentication authentication) {

        try {
            Page<MensajeResponse> mensajes;

            // Si hay búsqueda, filtrar por contenido
            if (busqueda != null && !busqueda.trim().isEmpty()) {
                mensajes = mensajeService.buscarMensajesUsuario(authentication, busqueda.trim(), page, size);
            }
            // Si hay filtro por tipo
            else if (tipo != null && !tipo.trim().isEmpty()) {
                try {
                    TipoMensaje tipoEnum = TipoMensaje.valueOf(tipo.toUpperCase());
                    mensajes = mensajeService.obtenerMensajesRecibidosPorTipo(authentication, tipoEnum, page, size);
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ApiResponse<>(false, "Tipo de mensaje inválido: " + tipo, null));
                }
            }
            // Obtener todos los mensajes recibidos
            else {
                mensajes = mensajeService.obtenerMensajesRecibidos(authentication, page, size);
            }

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Mensajes obtenidos exitosamente", mensajes));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener los mensajes: " + e.getMessage(), null));
        }
    }

    // Obtener todos los mensajes (solo administradores)
    @GetMapping("/admin/todos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<MensajeResponse>>> obtenerTodosLosMensajes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            Page<MensajeResponse> mensajes = mensajeService.obtenerTodosLosMensajes(page, size);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Mensajes obtenidos exitosamente", mensajes));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener los mensajes: " + e.getMessage(), null));
        }
    }

    // Obtener mensajes no leídos (solo administradores)
    @GetMapping("/admin/no-leidos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<MensajeResponse>>> obtenerMensajesNoLeidos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            Page<MensajeResponse> mensajes = mensajeService.obtenerMensajesNoLeidos(page, size);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Mensajes no leídos obtenidos exitosamente", mensajes));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener los mensajes: " + e.getMessage(), null));
        }
    }

    // Obtener mensajes por tipo (solo administradores)
    @GetMapping("/admin/tipo/{tipo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<MensajeResponse>>> obtenerMensajesPorTipo(
            @PathVariable TipoMensaje tipo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            Page<MensajeResponse> mensajes = mensajeService.obtenerMensajesPorTipo(tipo, page, size);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Mensajes obtenidos exitosamente", mensajes));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener los mensajes: " + e.getMessage(), null));

        }
    }


    // Buscar mensajes (solo administradores)
    @GetMapping("/admin/buscar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<MensajeResponse>>> buscarMensajes(
            @RequestParam String busqueda,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            Page<MensajeResponse> mensajes = mensajeService.buscarMensajes(busqueda, page, size);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Búsqueda completada exitosamente", mensajes));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error en la búsqueda: " + e.getMessage(), null));
        }
    }

    // Obtener mensaje por ID (solo administradores)
    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MensajeResponse>> obtenerMensajePorId(@PathVariable Long id) {

        try {
            return mensajeService.obtenerMensajePorId(id)
                    .map(mensaje -> ResponseEntity.ok(
                            new ApiResponse<>(true, "Mensaje obtenido exitosamente", mensaje)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ApiResponse<>(false, "Mensaje no encontrado", null)));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener el mensaje: " + e.getMessage(), null));
        }
    }


    // Marcar mensaje como leído (solo administradores)
    @PatchMapping("/{id}/marcar-leido")

    public ResponseEntity<ApiResponse<Void>> marcarComoLeido(@PathVariable Long id) {

        try {
            boolean marcado = mensajeService.marcarComoLeido(id);

            if (marcado) {
                return ResponseEntity.ok(
                        new ApiResponse<>(true, "Mensaje marcado como leído", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Mensaje no encontrado", null));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al marcar el mensaje: " + e.getMessage(), null));
        }
    }

    // Marcar mensaje como respondido (solo administradores)
    @PatchMapping("/admin/{id}/marcar-respondido")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> marcarComoRespondido(@PathVariable Long id) {

        try {
            boolean marcado = mensajeService.marcarComoRespondido(id);

            if (marcado) {
                return ResponseEntity.ok(
                        new ApiResponse<>(true, "Mensaje marcado como respondido", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Mensaje no encontrado", null));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al marcar el mensaje: " + e.getMessage(), null));
        }
    }

    // Eliminar mensaje (solo administradores)
    @DeleteMapping("/{id}")

    public ResponseEntity<ApiResponse<Void>> eliminarMensaje(@PathVariable Long id) {

        try {
            boolean eliminado = mensajeService.eliminarMensaje(id);

            if (eliminado) {
                return ResponseEntity.ok(
                        new ApiResponse<>(true, "Mensaje eliminado exitosamente", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Mensaje no encontrado", null));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al eliminar el mensaje: " + e.getMessage(), null));
        }
    }


    // Obtener estadísticas de mensajes (solo administradores)
    @GetMapping("/admin/estadisticas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EstadisticasMensajes>> obtenerEstadisticas() {

        try {
            EstadisticasMensajes estadisticas = mensajeService.obtenerEstadisticas();

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Estadísticas obtenidas exitosamente", estadisticas));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener estadísticas: " + e.getMessage(), null));
        }
    }

    // Obtener tipos de mensaje disponibles
    @GetMapping("/tipos")
    public ResponseEntity<ApiResponse<TipoMensaje[]>> obtenerTiposMensaje() {
        try {
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Tipos de mensaje obtenidos exitosamente", TipoMensaje.values()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener tipos de mensaje", null));
        }
    }
}
