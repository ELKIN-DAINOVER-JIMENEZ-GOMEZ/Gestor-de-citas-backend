package com.GestorDeCitas.Backend.DTOs.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EstadisticasMensajes {
    private long totalMensajes;
    private long mensajesNoLeidos;
    private long mensajesHoy;
    private long mensajesEstaWeek;
}
