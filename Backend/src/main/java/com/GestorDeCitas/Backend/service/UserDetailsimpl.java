package com.GestorDeCitas.Backend.service;

import com.GestorDeCitas.Backend.model.Usuarios;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor

public class UserDetailsimpl implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String username;
    private final String email;

    @JsonIgnore
    private final  String password;

    private final Collection<?extends GrantedAuthority> authorities;


    public static UserDetailsimpl build(Usuarios empleado){
        //Por defecto asignamos ROLE_USER, Puedes modificar esto segun tus necesidades
//        List<GrantedAuthority> authorities = Collections.singletonList(
//                new SimpleGrantedAuthority("ROLE_USER")
//        );  version anterior cuando solo se usaba un usuario



        List<GrantedAuthority> authorities = empleado.getRoles().stream()
                .map(roles -> new SimpleGrantedAuthority(roles.getRole().name()))
                .collect(Collectors.toList());
        return new UserDetailsimpl(

                empleado.getUsername(),
                empleado.getEmail(),
                empleado.getPassword(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }



    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Métodos equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsimpl user = (UserDetailsimpl) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }

}
