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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Optional;
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
        Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
        logger.info("=== INICIO AUTENTICACIÓN ===");
        logger.info("Usuario solicitado: {}", loginRequest.getUsername());

        try {
            // 1. Verificar si el usuario existe
            Optional<Users> userOpt = userRepository.findByUsername(loginRequest.getUsername());
            if (userOpt.isEmpty()) {
                logger.error("Usuario no encontrado: {}", loginRequest.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new MessageResponse("Credenciales incorrectas"));
            }

            Users user = userOpt.get();
            logger.info("Usuario encontrado: {} con rol: {}", user.getUsername(),
                    user.getRole() != null ? user.getRole().getRole() : "NULL");

            // 2. Intentar autenticación
            logger.info("Intentando autenticación con AuthenticationManager");
            Authentication authentication;
            try {
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(),
                                loginRequest.getPassword())
                );
                logger.info("Autenticación exitosa");
            } catch (Exception authEx) {
                logger.error("Fallo en autenticación: {}", authEx.getMessage());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new MessageResponse("Credenciales incorrectas"));
            }

            // 3. Establecer contexto de seguridad
            logger.info("Estableciendo contexto de seguridad");
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 4. Generar JWT
            logger.info("Generando JWT token");
            String jwt;
            try {
                jwt = jwtUtils.generateJwtToken(authentication);
                logger.info("JWT generado exitosamente");
            } catch (Exception jwtEx) {
                logger.error("Error generando JWT: {}", jwtEx.getMessage(), jwtEx);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new MessageResponse("Error generando token"));
            }

            // 5. Obtener detalles del usuario
            logger.info("Obteniendo detalles del usuario principal");
            UserDetailsImpl userDetails;
            try {
                userDetails = (UserDetailsImpl) authentication.getPrincipal();
                logger.info("UserDetails obtenido: {}", userDetails.getUsername());
            } catch (Exception userEx) {
                logger.error("Error obteniendo UserDetails: {}", userEx.getMessage(), userEx);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new MessageResponse("Error obteniendo detalles del usuario"));
            }

            // 6. Extraer roles
            logger.info("Extrayendo roles del usuario");
            List<String> roles;
            try {
                roles = userDetails.getAuthorities().stream()
                        .map(item -> item.getAuthority())
                        .collect(Collectors.toList());
                logger.info("Roles extraídos: {}", roles);
            } catch (Exception roleEx) {
                logger.error("Error extrayendo roles: {}", roleEx.getMessage(), roleEx);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new MessageResponse("Error extrayendo roles"));
            }

            // 7. Crear respuesta
            logger.info("Creando respuesta JWT");
            try {
                JwtResponse response = new JwtResponse(
                        jwt,
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles
                );
                logger.info("=== AUTENTICACIÓN EXITOSA ===");
                return ResponseEntity.ok(response);
            } catch (Exception respEx) {
                logger.error("Error creando respuesta: {}", respEx.getMessage(), respEx);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new MessageResponse("Error creando respuesta"));
            }

        } catch (Exception e) {
            logger.error("=== ERROR GENERAL EN AUTENTICACIÓN ===");
            logger.error("Tipo de excepción: {}", e.getClass().getSimpleName());
            logger.error("Mensaje: {}", e.getMessage());
            logger.error("Stack trace:", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error interno del servidor: " + e.getMessage()));
        }
    }
}