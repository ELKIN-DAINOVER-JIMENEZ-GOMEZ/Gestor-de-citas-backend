package com.GestorDeCitas.Backend.controller;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "https://gestor-de-citas-frontend-qvmc.vercel.app"})
public class HealthController {

    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

    // Endpoint raíz para health checks de Render
    @GetMapping("/")
    public ResponseEntity<?> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "Gestor de Citas Backend");
        response.put("status", "UP");
        response.put("version", "1.0");
        return ResponseEntity.ok(response);
    }

    // Health check específico
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(health);
    }

    // Manejar todas las peticiones HEAD (health checks de Render)
    @RequestMapping(method = RequestMethod.HEAD, value = "/**")
    public ResponseEntity<Void> handleHeadRequests() {
        return ResponseEntity.ok().build();
    }

    // Manejar peticiones OPTIONS
    @RequestMapping(method = RequestMethod.OPTIONS, value = "/**")
    public ResponseEntity<Void> handleOptionsRequests() {
        return ResponseEntity.ok().build();
    }
}