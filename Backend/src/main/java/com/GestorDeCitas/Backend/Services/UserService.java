package com.GestorDeCitas.Backend.Services;


import com.GestorDeCitas.Backend.DTOs.response.UserInfoResponse;
import com.GestorDeCitas.Backend.models.Roles.ERole;
import com.GestorDeCitas.Backend.models.Users;
import com.GestorDeCitas.Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    /**
     * Obtiene una lista de usuarios según el rol especificado.
     * @param role El rol a buscar (ej. Role.ROLE_ADMIN, Role.ROLE_USER).
     * @return Una lista de UserInfoResponse.
     */
    public List<UserInfoResponse> getUsersByRole(ERole role) {
        List<Users> empleados = userRepository.findByRole_Role(role);
        return empleados.stream()
                .map(UserInfoResponse::new)
                .collect(Collectors.toList());
    }
}
