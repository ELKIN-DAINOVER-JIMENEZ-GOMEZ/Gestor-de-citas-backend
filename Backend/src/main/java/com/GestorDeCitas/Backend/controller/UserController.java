package com.GestorDeCitas.Backend.controller;


import com.GestorDeCitas.Backend.DTOs.response.UserInfoResponse;
import com.GestorDeCitas.Backend.Services.UserService;
import com.GestorDeCitas.Backend.models.Roles.ERole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:5173" , "https://gestor-de-citas-dental-care.onrender.com"})
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/admins")
    @PreAuthorize("hasRole('PACIENTE') or hasRole('ADMIN')") // Solo usuarios autenticados pueden ver la lista
    public ResponseEntity<List<UserInfoResponse>> getAdmins() {
        List<UserInfoResponse> admins = userService.getUsersByRole(ERole.ROLE_ADMIN);
        return ResponseEntity.ok(admins);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')") // Solo administradores pueden ver esta lista
    public ResponseEntity<List<UserInfoResponse>> getUsers() {
        List<UserInfoResponse> users = userService.getUsersByRole(ERole.ROLE_PACIENTE);
        return ResponseEntity.ok(users);
    }
}
