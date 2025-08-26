package com.GestorDeCitas.Backend.models.Mensajes;

import java.time.LocalDateTime;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "mensajes")
public class Mensaje {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "remintente_id",  nullable= false)
    private Long remitenteId;

    @Column(name= "remitente_nombre", nullable= false)
    private String remitenteNombre;

    @Column(name= "remitente_email", nullable= false)
    private String remitenteEmail;

    @Column(name= "destinatario_id")
    private Long destinatarioId;

    // Nuevo campo: información del destinatario específico
    @Column(name= "destinatario_nombre")
    private String destinatarioNombre;

    @Column(name= "destinatario_email")
    private String destinatarioEmail;

    @Column ( name = "asunto", nullable = false)
    private String asunto;

    @Column ( name = "contenido", nullable = false)
    private String contenido;

    @Column ( name = "leido", nullable = false)
    private Boolean leido = false;

    @Column ( name = "respondido", nullable = false)
    private Boolean respondido = false;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;

    @Column(name = "fecha_lectura")
    private LocalDateTime fechaLectura;

    @Column(name = "tipo_mensaje", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoMensaje tipoMensaje;

    @Column(name = "prioridad", nullable = false)
    @Enumerated(EnumType.STRING)
    private PrioridadMensaje prioridad = PrioridadMensaje.NORMAL;


    // Constructor para mensaje dirigido a usuario específico
    public Mensaje(Long remitenteId, String remitenteNombre, String remitenteEmail,
                   Long destinatarioId, String destinatarioNombre, String destinatarioEmail,
                   String asunto, String contenido, TipoMensaje tipoMensaje) {
        this.remitenteId = remitenteId;
        this.remitenteNombre = remitenteNombre;
        this.remitenteEmail = remitenteEmail;
        this.destinatarioId = destinatarioId;
        this.destinatarioNombre = destinatarioNombre;
        this.destinatarioEmail = destinatarioEmail;
        this.asunto = asunto;
        this.contenido = contenido;
        this.tipoMensaje = tipoMensaje;
        this.fechaEnvio = LocalDateTime.now();
        this.leido = false;
        this.respondido = false;
        this.prioridad = PrioridadMensaje.NORMAL;
    }

}
