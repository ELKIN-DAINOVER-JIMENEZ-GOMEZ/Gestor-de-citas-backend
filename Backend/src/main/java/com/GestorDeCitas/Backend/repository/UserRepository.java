package com.GestorDeCitas.Backend.repository;

import com.GestorDeCitas.Backend.model.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository {

    Optional<Usuarios> findByUsername(String username);

    Optional<Usuarios> findByEmail(String email);

    //verificar si existe usuario
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
