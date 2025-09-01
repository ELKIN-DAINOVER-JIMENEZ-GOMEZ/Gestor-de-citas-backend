package com.GestorDeCitas.Backend.models;

import com.GestorDeCitas.Backend.models.Roles.Roles;
import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor

@ToString

@Table(name = "usuarios",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"email"})//Evita que se repita el email
        })
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank
    @Size(min = 1, max = 20)
    private String username;

    @NotBlank
    @Size(min = 1, max = 50)
    @Email
    private String email;

    private String password;



    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "role_id")//Hace referencia a que la tabla roles tiene un campo id
    private Roles role;

    public Users(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }


}
