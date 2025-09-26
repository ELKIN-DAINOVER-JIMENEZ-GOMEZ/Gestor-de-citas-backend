package com.GestorDeCitas.Backend.config;


import com.GestorDeCitas.Backend.Services.UserDetailsServiceImpl;
import com.GestorDeCitas.Backend.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Bean
    public AuthTokenFilter authenticationTokenFilter(){
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration authConfig)throws Exception{
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception{
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))//configuracion moderna CORS
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth ->
                        auth
                                // Rutas públicas de autenticación
                                .requestMatchers("/api/auth/**").permitAll()


                                // NUEVAS RUTAS PARA CITAS - Pacientes (ROLE_PACIENTE)
                                .requestMatchers("/api/citas").hasAnyRole("PACIENTE", "ADMIN")
                                .requestMatchers("/api/citas/mis-citas").hasRole("PACIENTE")
                                .requestMatchers("/api/citas/proximas").hasRole("PACIENTE")


                                // RUTAS ADMINISTRATIVAS PARA CITAS - Solo Admin (ROLE_ADMIN)
                                .requestMatchers("/api/citas/todas").hasRole("ADMIN")
                                .requestMatchers("/api/citas/fecha/**").hasRole("ADMIN")
                                .requestMatchers("/api/citas/*/estado").hasRole("ADMIN")
                                .requestMatchers("/api/citas/*/cancelar").hasAnyRole("PACIENTE", "ADMIN")

                                .requestMatchers("/api/citas/*").hasAnyRole("PACIENTE","ADMIN")
                                .requestMatchers("/api/citas/**").hasRole("ADMIN")


                                // Rutas existentes
                                .requestMatchers("/api/auth/mis-citas/**").hasRole("PACIENTE")
                                .requestMatchers("/api/auth/gestion/**").hasRole("ADMIN")

                                .anyRequest().authenticated()
                )
                .authenticationProvider(daoAuthenticationProvider())
                .addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Asegúrate de cambiar "http://localhost:4200" por el origen de tu frontend

        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "https://gestor-de-citas-frontend-qvmc.vercel.app"
        ));

        //Metodos http permitidos
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        //Headers permitidos
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"));

        //Headers expuestos al cliente
        configuration.setExposedHeaders(Arrays.asList(
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials"
        ));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); //Cache preflight por 1 hora


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
