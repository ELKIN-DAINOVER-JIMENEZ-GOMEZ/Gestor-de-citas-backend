package com.GestorDeCitas.Backend.dto.request;





import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "El nombre de usuario no puede estar en blanco")
    private String username;// Esto es para indicar que no puede estar en blanco

    @NotBlank
    private String password;

}
