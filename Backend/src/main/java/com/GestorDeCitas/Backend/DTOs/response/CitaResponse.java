package com.GestorDeCitas.Backend.DTOs.response;

import com.GestorDeCitas.Backend.models.Citas.EstadoCita;
import com.GestorDeCitas.Backend.DTOs.response.UserInfoResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CitaResponse {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd" )
    private LocalDate fecha;


    @JsonFormat(pattern = "HH:mm")
    private LocalTime hora;

    private String servicio;
    private String notas;
    private EstadoCita estado;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaCreacion;

    private UserInfoResponse usuario;



}
