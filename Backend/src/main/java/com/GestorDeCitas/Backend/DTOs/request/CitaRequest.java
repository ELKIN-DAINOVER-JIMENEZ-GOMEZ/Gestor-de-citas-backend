package com.GestorDeCitas.Backend.DTOs.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CitaRequest {
    @NotNull(message = "La fecha es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime hora;


    @NotBlank(message = "El servicio es obligatorio")
    private String servicio;

    private String notas;

}
