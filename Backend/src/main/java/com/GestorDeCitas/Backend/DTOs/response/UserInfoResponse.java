package com.GestorDeCitas.Backend.DTOs.response;

import com.GestorDeCitas.Backend.models.Users;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInfoResponse {
    private Long id;
    private String username;
    private String email;

    // Constructor que toma la entidad user
    public UserInfoResponse(Users usuario) {
        this.id = usuario.getId();
        this.username = usuario.getUsername();
        this.email = usuario.getEmail();
    }
}
