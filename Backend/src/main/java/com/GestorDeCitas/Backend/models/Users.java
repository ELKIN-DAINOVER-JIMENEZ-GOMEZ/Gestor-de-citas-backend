package com.GestorDeCitas.Backend.models;



@Data
@NoArgsConstructor

@ToString

@Table(name = "usuarios",
        uniqueConstraints = {
                // Puedes agregar restricciones únicas aquí si es necesario
                @UniqueConstraint(columnNames = {"username"}),//Esto es para evitar que se repita el nombre de usuario
                @UniqueConstraint(columnNames = {"email"})//Esto es para evitar que se repita el email
        })
public class Empleado {
    @Id// Esto es para indicar que es la llave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank// Esto es para indicar que no puede estar en blanco
    @Size(min = 1, max = 20)// Esto es para indicar el tamaño mínimo y máximo
    private String username;

    @NotBlank
    @Size(min = 1, max = 50)
    @Email// Esto es para indicar que debe ser un email válido
    private String email;

    private String password;



    @ManyToMany(fetch= FetchType.EAGER)
    @JoinTable(name = "usuarios_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private java.util.Set<Roles> roles = new java.util.HashSet<>();

    public Empleado(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }