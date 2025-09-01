package com.GestorDeCitas.Backend.Services;

import com.GestorDeCitas.Backend.models.Users;
import com.GestorDeCitas.Backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername (String username )throws UsernameNotFoundException {
        Users empleado = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el nombre: " + username));


        return UserDetailsImpl.build(empleado);
    }

    @Transactional
    public Users obtenerPorUsername (String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el nombre: " + username));
    }

    public Users obtenerPorId(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
