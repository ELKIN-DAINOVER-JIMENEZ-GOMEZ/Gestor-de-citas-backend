
package com.GestorDeCitas.Backend.config;

import com.GestorDeCitas.Backend.models.Roles.ERole;
import com.GestorDeCitas.Backend.models.Roles.Roles;
import com.GestorDeCitas.Backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Inicializar roles si no existen
        initRoles();
    }

    private void initRoles() {
        try {
            // Verificar y crear rol PACIENTE
            if (roleRepository.findByRole(ERole.ROLE_PACIENTE).isEmpty()) {
                Roles pacienteRole = new Roles();
                pacienteRole.setRole(ERole.ROLE_PACIENTE);
                roleRepository.save(pacienteRole);
                System.out.println("Rol ROLE_PACIENTE creado exitosamente");
            }

            // Verificar y crear rol ADMIN
            if (roleRepository.findByRole(ERole.ROLE_ADMIN).isEmpty()) {
                Roles adminRole = new Roles();
                adminRole.setRole(ERole.ROLE_ADMIN);
                roleRepository.save(adminRole);
                System.out.println("Rol ROLE_ADMIN creado exitosamente");
            }

            System.out.println("Inicialización de roles completada");

        } catch (Exception e) {
            System.err.println("Error al inicializar roles: " + e.getMessage());
            e.printStackTrace();
        }
    }
}