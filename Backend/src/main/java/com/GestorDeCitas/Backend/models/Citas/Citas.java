package com.GestorDeCitas.Backend.models.Citas;

import com.GestorDeCitas.Backend.models.Users;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Users usuario;


     @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaActualizacion;



    public Citas() {
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoCita.PENDIENTE;
    }


    // Constructor con parámetros
    public Citas(LocalDate fecha, LocalTime hora, String servicio, String notas, Users usuario) {
        this.fecha = fecha;
        this.hora = hora;
        this.servicio = servicio;
        this.notas = notas;
        this.usuario = usuario;
    }

}
