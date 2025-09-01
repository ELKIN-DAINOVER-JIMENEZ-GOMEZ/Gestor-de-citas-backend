package com.GestorDeCitas.Backend.Services;

import com.GestorDeCitas.Backend.DTOs.request.LoginRequest;
import com.GestorDeCitas.Backend.DTOs.request.SignUpRequest;
import com.GestorDeCitas.Backend.DTOs.response.JwtResponse;
import com.GestorDeCitas.Backend.DTOs.response.MessageResponse;
import com.GestorDeCitas.Backend.models.Roles.ERole;
import com.GestorDeCitas.Backend.models.Roles.Roles;
import com.GestorDeCitas.Backend.models.Users;
import com.GestorDeCitas.Backend.repository.AuthService;
import com.GestorDeCitas.Backend.repository.RoleRepository;
import com.GestorDeCitas.Backend.repository.UserRepository;
import com.GestorDeCitas.Backend.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public ResponseEntity<?>registerUser(SignUpRequest signUpRequest) {

        // Verificar si el username existe
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: El nombre de usuario ya existe"));
        }

        // Verificar si el email ya existe
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: El email ya existe"));
        }

        // Verificar permisos si se intenta crear un admin
        if ("admin".equalsIgnoreCase(signUpRequest.getRole())) {

            // Permitir crear el primer administrador si no existe ninguno
            boolean adminExists = userRepository.existsByRole_Role(ERole.ROLE_ADMIN);
            //Verificar si el usuario que hace la solicitud es un administrador
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (adminExists && (authentication == null || !authentication.getAuthorities().stream()
                    .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN")))) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(new MessageResponse("Error: Un administrador ya existe. Solo otro administrador puede crear más cuentas."));
            }


        }

        // Crear cuenta de usuario
        Users user = new Users(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword())

        );

        // Asignar role
        Roles userRole;

        if ("admin".equalsIgnoreCase(signUpRequest.getRole())) {
            userRole = roleRepository.findByRole(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Rol de administrador no encontrado."));
        } else {
            userRole = roleRepository.findByRole(ERole.ROLE_PACIENTE)
                    .orElseThrow(() -> new RuntimeException("Error: Rol de paciente no encontrado."));
        }

        //Asignar el rol directamente al usuario
        user.setRole(userRole);
        //Guardar en base de datos
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Usuario registrado correctamente"));
    }

    @Override
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new JwtResponse(
                    jwt,
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles // Incluir los roles en la respuesta
            ));

        } catch (AuthenticationException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Credenciales incorrectas"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error interno del servidor"));
        }
    }
}