package com.GestorDeCitas.Backend.repository;

import com.GestorDeCitas.Backend.DTOs.request.LoginRequest;
import com.GestorDeCitas.Backend.DTOs.request.SignUpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthService {
    ResponseEntity<?>registerUser(SignUpRequest signUpRequest);
    ResponseEntity<?> authenticateUser(LoginRequest loginRequest);
}
