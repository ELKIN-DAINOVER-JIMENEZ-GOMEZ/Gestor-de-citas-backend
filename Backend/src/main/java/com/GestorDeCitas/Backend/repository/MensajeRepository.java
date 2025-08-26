package com.GestorDeCitas.Backend.repository;

import com.GestorDeCitas.Backend.models.Mensajes.Mensaje;
import com.GestorDeCitas.Backend.models.Mensajes.TipoMensaje;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    // Obtener mensajes enviados por un usuario específico
    Page<Mensaje> findByRemitenteIdOrderByFechaEnvioDesc(Long remitenteId, Pageable pageable);

    // Obtener todos los mensajes para administradores (ordenados por fecha)
    Page<Mensaje> findAllByOrderByFechaEnvioDesc(Pageable pageable);

    // Obtener mensajes no leídos para administradores
    Page<Mensaje> findByLeidoFalseOrderByFechaEnvioDesc(Pageable pageable);

    // Obtener mensajes por tipo
    Page<Mensaje> findByTipoMensajeOrderByFechaEnvioDesc(TipoMensaje tipoMensaje, Pageable pageable);

    // Obtener mensajes por rango de fechas
    Page<Mensaje> findByFechaEnvioBetweenOrderByFechaEnvioDesc(
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            Pageable pageable
    );

    //Obtener mensajes por id
    Page<Mensaje> findByDestinatarioIdOrderByFechaEnvioDesc(Long destinatarioId, Pageable pageable);

    // Contar mensajes no leídos
    long countByLeidoFalse();

    // Contar mensajes de un usuario
    long countByRemitenteId(Long remitenteId);

    // Marcar mensaje como leído
    @Modifying
    @Query("UPDATE Mensaje m SET m.leido = true, m.fechaLectura = :fechaLectura WHERE m.id = :id")
    void marcarComoLeido(@Param("id") Long id, @Param("fechaLectura") LocalDateTime fechaLectura);

    // Marcar mensaje como respondido
    @Modifying
    @Query("UPDATE Mensaje m SET m.respondido = true WHERE m.id = :id")
    void marcarComoRespondido(@Param("id") Long id);

    // Buscar mensajes por contenido o asunto
    @Query("SELECT m FROM Mensaje m WHERE " +
            "LOWER(m.asunto) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(m.contenido) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(m.remitenteNombre) LIKE LOWER(CONCAT('%', :busqueda, '%')) " +
            "ORDER BY m.fechaEnvio DESC")
    Page<Mensaje> buscarMensajesPorContenidoOAsunto(@Param("busqueda") String busqueda, Pageable pageable);



    // Buscar mensajes por usuario y contenido
    @Query("SELECT m FROM Mensaje m WHERE m.destinatarioId = :destinatarioId " +
            "AND (LOWER(m.asunto) LIKE LOWER(CONCAT('%', :busqueda, '%')) " +
            "OR LOWER(m.contenido) LIKE LOWER(CONCAT('%', :busqueda, '%'))) " +
            "ORDER BY m.fechaEnvio DESC")
    Page<Mensaje> buscarMensajesPorUsuarioYContenido(
            @Param("destinatarioId") Long destinatarioId,
            @Param("busqueda") String busqueda,
            Pageable pageable
    );

    // Obtener mensajes por destinatario y tipo
    Page<Mensaje> findByDestinatarioIdAndTipoMensajeOrderByFechaEnvioDesc(
            Long destinatarioId,
            TipoMensaje tipoMensaje,
            Pageable pageable
    );
}
