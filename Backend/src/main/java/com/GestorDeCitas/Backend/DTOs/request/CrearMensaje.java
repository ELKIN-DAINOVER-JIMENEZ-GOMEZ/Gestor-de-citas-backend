package com.GestorDeCitas.Backend.DTOs.request;

import com.GestorDeCitas.Backend.models.Mensajes.PrioridadMensaje;
import com.GestorDeCitas.Backend.models.Mensajes.TipoMensaje;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class CrearMensaje {
    @NotBlank(message = "El asunto es obligatorio")
    @Size(max = 255, message = "El asunto no puede exceder 255 caracteres")
    private String asunto;

    @NotBlank(message = "El contenido es obligatorio")
    @Size(max = 5000, message = "El contenido no puede exceder 5000 caracteres")
    private String contenido;

    // Nuevo campo para especificar el destinatario
    @NotNull(message = "El destinatario es obligatorio")
    private Long destinatarioId;

    @NotNull(message = "El destinatario es obligatorio")
    private String destinatarioNombre;

    @NotNull(message = "El destinatario es obligatorio")
    private String destinatarioEmail;

    @NotNull(message = "El tipo de mensaje es obligatorio")
    private TipoMensaje tipoMensaje;



    private PrioridadMensaje prioridad = PrioridadMensaje.NORMAL;


    //Constructor
    public CrearMensaje(String asunto, String contenido, Long destinatarioId, String destinatarioNombre, String destinatarioEmail, TipoMensaje tipoMensaje) {
        this.asunto = asunto;
        this.contenido = contenido;
        this.destinatarioId = destinatarioId;
        this.destinatarioNombre = destinatarioNombre;
        this.destinatarioEmail = destinatarioEmail;
        this.tipoMensaje = tipoMensaje;
    }
}
