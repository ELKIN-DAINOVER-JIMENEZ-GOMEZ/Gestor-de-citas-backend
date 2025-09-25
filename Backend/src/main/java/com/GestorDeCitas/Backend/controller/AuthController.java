package com.GestorDeCitas.Backend.controller;

import com.GestorDeCitas.Backend.DTOs.request.LoginRequest;
import com.GestorDeCitas.Backend.DTOs.request.SignUpRequest;
import com.GestorDeCitas.Backend.DTOs.response.MessageResponse;
import com.GestorDeCitas.Backend.models.Users;
import com.GestorDeCitas.Backend.repository.AuthService;
import com.GestorDeCitas.Backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:5173", "https://gestor-de-citas-frontend-qvmc.vercel.app"},
        allowCredentials = "true",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @GetMapping("/users") // SOLO PARA DESARROLLO
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("POST /signin - Login attempt for user: {}", loginRequest.getUsername());
        try {
            return authService.authenticateUser(loginRequest);
        } catch (Exception e) {
            logger.error("Error in signin: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error en el login: " + e.getMessage()));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        logger.info("POST /signup - Registration attempt for user: {}", signUpRequest.getUsername());
        try {
            return authService.registerUser(signUpRequest);
        } catch (Exception e) {
            logger.error("Error in signup: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error en el registro: " + e.getMessage()));
        }
    }

    // Endpoint específico para manejar OPTIONS
    @RequestMapping(method = RequestMethod.OPTIONS, value = "/**")
    public ResponseEntity<?> handleOptions() {
        logger.info("OPTIONS request received");
        return ResponseEntity.ok().build();
    }

    // Endpoint para manejar HEAD requests (health checks de Render)
    @RequestMapping(method = RequestMethod.HEAD, value = "/**")
    public ResponseEntity<?> handleHead() {
        return ResponseEntity.ok().build();
    }

    // Root endpoint para health checks
    @GetMapping("/")
    public ResponseEntity<?> root() {
        return ResponseEntity.ok(new MessageResponse("Gestor de Citas API - Running"));
    }

    // Endpoint de test para verificar conectividad
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok(new MessageResponse("API funcionando correctamente"));
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        try {
            // Verificar conexión a base de datos
            long userCount = userRepository.count();
            return ResponseEntity.ok(new MessageResponse("API healthy - Users in DB: " + userCount));
        } catch (Exception e) {
            logger.error("Health check failed: {}", e.getMessage());
            return ResponseEntity.status(503)
                    .body(new MessageResponse("API unhealthy: " + e.getMessage()));
        }
    }

    // Debug endpoint - temporal
    @GetMapping("/debug")
    public ResponseEntity<?> debug() {
        try {
            long userCount = userRepository.count();
            // Contar roles si tienes el repository inyectado
            Map<String, Object> debug = new HashMap<>();
            debug.put("userCount", userCount);
            debug.put("timestamp", new Date());
            debug.put("status", "OK");
            return ResponseEntity.ok(debug);
        } catch (Exception e) {
            logger.error("Debug endpoint failed: {}", e.getMessage());
            return ResponseEntity.status(500)
                    .body(new MessageResponse("Debug failed: " + e.getMessage()));
        }
    }
}