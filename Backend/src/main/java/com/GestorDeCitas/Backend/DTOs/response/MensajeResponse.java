package com.GestorDeCitas.Backend.DTOs.response;

import com.GestorDeCitas.Backend.models.Mensajes.Mensaje;
import com.GestorDeCitas.Backend.models.Mensajes.PrioridadMensaje;
import com.GestorDeCitas.Backend.models.Mensajes.TipoMensaje;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class MensajeResponse {
    private Long id;
    private Long remitenteId;
    private String remitenteNombre;
    private String remitenteEmail;

    //Información del destinatario específico
    private Long destinatarioId;
    private String destinatarioNombre;
    private String destinatarioEmail;

    private String asunto;
    private String contenido;
    private Boolean leido;
    private Boolean respondido;
    private LocalDateTime fechaEnvio;
    private LocalDateTime fechaLectura;
    private TipoMensaje tipoMensaje;
    private PrioridadMensaje prioridad;


    // Constructor desde entidad
    public MensajeResponse(Mensaje mensaje) {
        this.id = mensaje.getId();
        this.remitenteId = mensaje.getRemitenteId();
        this.remitenteNombre = mensaje.getRemitenteNombre();
        this.remitenteEmail = mensaje.getRemitenteEmail();

        this.destinatarioId = mensaje.getDestinatarioId();
        this.destinatarioNombre = mensaje.getDestinatarioNombre();
        this.destinatarioEmail = mensaje.getDestinatarioEmail();

        this.asunto = mensaje.getAsunto();
        this.contenido = mensaje.getContenido();
        this.leido = mensaje.getLeido();
        this.respondido = mensaje.getRespondido();
        this.fechaEnvio = mensaje.getFechaEnvio();
        this.fechaLectura = mensaje.getFechaLectura();
        this.tipoMensaje = mensaje.getTipoMensaje();
        this.prioridad = mensaje.getPrioridad();
    }
}
