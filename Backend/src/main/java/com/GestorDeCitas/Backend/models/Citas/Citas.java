package com.GestorDeCitas.Backend.models.Citas;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "citas")
public class Citas {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La fecha es obligatoria")
    @Column(name= "fecha", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    @Column(name = "hora", nullable = false)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime hora;

    @NotBlank(message = "El servicio es obligatorio")
    @Column(name = "servicio", nullable = false, length = 100)
    private String servicio;

    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoCita estado = EstadoCita.PENDIENTE;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaCreacion;

     @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaActualizacion;

}
