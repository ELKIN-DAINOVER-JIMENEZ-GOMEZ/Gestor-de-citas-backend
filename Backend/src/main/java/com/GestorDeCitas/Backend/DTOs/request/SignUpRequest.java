package com.GestorDeCitas.Backend.DTOs.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SignUpRequest {

    private Long id;

    @NotBlank
    @Size(min = 3, max = 20)
    private String username; // Nombre de usuario del nuevo empleado

    @NotBlank
    @Size(min = 5, max = 50)
    private String email; // Correo electrónico del nuevo empleado

    @NotBlank
    @Size(min = 6, max = 19)
    private String password; // Contraseña del nuevo empleado

    private String role;// Rol del nuevo empleado
}
