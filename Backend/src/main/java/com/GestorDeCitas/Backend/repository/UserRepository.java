package com.GestorDeCitas.Backend.repository;

import com.GestorDeCitas.Backend.models.Roles.ERole;
import com.GestorDeCitas.Backend.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    Optional<Users> findByEmail(String email);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    boolean existsByRoles_Role(ERole role);
    List<Users> findByRoles_Role(ERole role);
}
