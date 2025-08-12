package com.GestorDeCitas.Backend.dto.request;



import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;


@Data
public class SignUpRequest {
private Long id;

@NotBlank
    @Size(min = 1, max = 20)
    private String username;

    @NotBlank
    @Size(min = 1, max = 50)
    private String email;

    @NotBlank
    @Size(min = 1, max = 100)
    private String password;

    private String role;

}
