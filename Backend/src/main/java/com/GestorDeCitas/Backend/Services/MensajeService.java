package com.GestorDeCitas.Backend.Services;

import com.GestorDeCitas.Backend.DTOs.request.CrearMensaje;
import com.GestorDeCitas.Backend.DTOs.response.EstadisticasMensajes;
import com.GestorDeCitas.Backend.DTOs.response.MensajeResponse;
import com.GestorDeCitas.Backend.models.Mensajes.Mensaje;
import com.GestorDeCitas.Backend.models.Mensajes.TipoMensaje;
import com.GestorDeCitas.Backend.repository.MensajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

public class MensajeService {
    @Autowired
    private MensajeRepository mensajeRepositroy;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    // Crear nuevo mensaje (desde paciente a admin)
    public MensajeResponse crearMensaje (CrearMensaje crearMensaje, Authentication authentication){
        // Obtener información del usuario autenticado

        String name = authentication.getName();
        var usuario = userDetailsService.obtenerPorUsername(name);


        Long destinatarioId = crearMensaje.getDestinatarioId();


        // Validar destinatario
        if (destinatarioId == null) {
            throw new IllegalArgumentException("destinatarioId es obligatorio");


        }
        // Mensaje dirigido a usuario específico
        var destinatario = userDetailsService.obtenerPorId(crearMensaje.getDestinatarioId());
        if (destinatario == null) {
            throw new RuntimeException("Usuario destinatario no encontrado");
        }

        Mensaje mensaje = new Mensaje(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                destinatario.getId(),
                destinatario.getUsername(),
                destinatario.getEmail(),
                crearMensaje.getAsunto(),
                crearMensaje.getContenido(),
                crearMensaje.getTipoMensaje()
        );

        mensaje.setPrioridad(crearMensaje.getPrioridad());

        Mensaje mensajeGuardado= mensajeRepositroy.save(mensaje);
        return new MensajeResponse(mensajeGuardado);
    }



    // Obtener mensajes recibidos por el usuario autenticado (más seguro: no pasa id)
    public Page<MensajeResponse> obtenerMensajesRecibidos(Authentication authentication, int page, int size) {
        String username = authentication.getName();
        var usuario = userDetailsService.obtenerPorUsername(username);

        Pageable pageable = PageRequest.of(page, size);
        Page<Mensaje> mensajes = mensajeRepositroy
                .findByDestinatarioIdOrderByFechaEnvioDesc(usuario.getId(), pageable);

        return mensajes.map(MensajeResponse::new);
    }

    // Variante: recibe destinatarioId pero verifica que sea el del usuario autenticado
    public Page<MensajeResponse> obtenerMensajesRecibidosPorId(Long destinatarioId,
                                                               Authentication authentication,
                                                               int page, int size) {
        String username = authentication.getName();
        var usuario = userDetailsService.obtenerPorUsername(username);

        if (!usuario.getId().equals(destinatarioId)) {
            // Opcional: puedes usar AccessDeniedException si lo prefieres
            throw new RuntimeException("No tienes permiso para ver estos mensajes");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Mensaje> mensajes = mensajeRepositroy
                .findByDestinatarioIdOrderByFechaEnvioDesc(destinatarioId, pageable);

        return mensajes.map(MensajeResponse::new);
    }


    // Obtener mensajes enviados por un usuario

    public Page<MensajeResponse> obtenerMensajesUsuario(Authentication authentication, int page, int size) {
        String name = authentication.getName();
        var usuario = userDetailsService.obtenerPorUsername(name);

        Pageable pageable = PageRequest.of(page, size);
        Page<Mensaje> mensajes = mensajeRepositroy.findByRemitenteIdOrderByFechaEnvioDesc(usuario.getId(), pageable);

        return mensajes.map(MensajeResponse::new);
    }

    // Obtener todos los mensajes (para administradores)

    public Page<MensajeResponse> obtenerTodosLosMensajes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Mensaje> mensajes = mensajeRepositroy.findAllByOrderByFechaEnvioDesc(pageable);

        return mensajes.map(MensajeResponse::new);
    }


    // Obtener mensajes por tipo
    public Page<MensajeResponse> obtenerMensajesPorTipo(TipoMensaje tipo, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Mensaje> mensajes = mensajeRepositroy.findByTipoMensajeOrderByFechaEnvioDesc(tipo, pageable);

        return mensajes.map(MensajeResponse::new);
    }
    // Buscar mensajes
    public Page<MensajeResponse> buscarMensajes(String busqueda, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Mensaje> mensajes = mensajeRepositroy.buscarMensajesPorContenidoOAsunto(busqueda, pageable);

        return mensajes.map(MensajeResponse::new);
    }

    // Obtener mensaje por ID
    public Optional<MensajeResponse> obtenerMensajePorId(Long id) {
        return mensajeRepositroy.findById(id)
                .map(MensajeResponse::new);
    }

    // Obtener mensajes no leídos (para administradores)
    public Page<MensajeResponse> obtenerMensajesNoLeidos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Mensaje> mensajes = mensajeRepositroy.findByLeidoFalseOrderByFechaEnvioDesc(pageable);
        return mensajes.map(MensajeResponse::new);
    }


    // Marcar mensaje como leído
    public boolean marcarComoLeido(Long id) {
        Optional<Mensaje> mensajeOpt = mensajeRepositroy.findById(id);
        if (mensajeOpt.isPresent()) {
            mensajeRepositroy.marcarComoLeido(id, LocalDateTime.now());
            return true;
        }
        return false;
    }


    // Marcar mensaje como respondido
    public boolean marcarComoRespondido(Long id) {
        Optional<Mensaje> mensajeOpt = mensajeRepositroy.findById(id);
        if (mensajeOpt.isPresent()) {
            mensajeRepositroy.marcarComoRespondido(id);
            return true;
        }
        return false;
    }

    // Eliminar mensaje
    public boolean eliminarMensaje(Long id) {
        if (mensajeRepositroy.existsById(id)) {
            mensajeRepositroy.deleteById(id);
            return true;
        }
        return false;
    }

    // Obtener estadísticas de mensajes
    public EstadisticasMensajes obtenerEstadisticas() {
        long totalMensajes = mensajeRepositroy.count();
        long mensajesNoLeidos = mensajeRepositroy.countByLeidoFalse();

        // Mensajes de hoy
        LocalDateTime inicioHoy = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime finHoy = LocalDateTime.now().with(LocalTime.MAX);
        long mensajesHoy = mensajeRepositroy.findByFechaEnvioBetweenOrderByFechaEnvioDesc(
                inicioHoy, finHoy, Pageable.unpaged()
        ).getTotalElements();

        // Mensajes de esta semana
        LocalDateTime inicioWeek = LocalDateTime.now().minusDays(7).with(LocalTime.MIN);
        LocalDateTime finWeek = LocalDateTime.now().with(LocalTime.MAX);
        long mensajesEstaWeek = mensajeRepositroy.findByFechaEnvioBetweenOrderByFechaEnvioDesc(
                inicioWeek, finWeek, Pageable.unpaged()
        ).getTotalElements();

        return new EstadisticasMensajes(totalMensajes, mensajesNoLeidos, mensajesHoy, mensajesEstaWeek);
    }





    // Buscar mensajes del usuario autenticado
    public Page<MensajeResponse> buscarMensajesUsuario(Authentication authentication, String busqueda, int page, int size) {
        String username = authentication.getName();
        var usuario = userDetailsService.obtenerPorUsername(username);

        Pageable pageable = PageRequest.of(page, size);

        // Buscar en los mensajes recibidos por el usuario
        Page<Mensaje> mensajes = mensajeRepositroy.buscarMensajesPorUsuarioYContenido(
                usuario.getId(), busqueda, pageable
        );

        return mensajes.map(MensajeResponse::new);
    }

    // Obtener mensajes recibidos por tipo para un usuario
    public Page<MensajeResponse> obtenerMensajesRecibidosPorTipo(Authentication authentication, TipoMensaje tipo, int page, int size) {
        String username = authentication.getName();
        var usuario = userDetailsService.obtenerPorUsername(username);

        Pageable pageable = PageRequest.of(page, size);

        // Obtener mensajes recibidos por tipo
        Page<Mensaje> mensajes = mensajeRepositroy.findByDestinatarioIdAndTipoMensajeOrderByFechaEnvioDesc(
                usuario.getId(), tipo, pageable
        );

        return mensajes.map(MensajeResponse::new);
    }
}
