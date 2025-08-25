package com.GestorDeCitas.Backend.repository;

import java.util.Optional;

import com.GestorDeCitas.Backend.models.Roles.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import com.GestorDeCitas.Backend.models.Roles.Roles;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository <Roles, Integer> {
    Optional<Roles> findByRole(ERole role);
}
