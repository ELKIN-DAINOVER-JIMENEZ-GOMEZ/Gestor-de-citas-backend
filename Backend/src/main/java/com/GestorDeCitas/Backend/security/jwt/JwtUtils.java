package com.GestorDeCitas.Backend.security.jwt;



import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);


    @Value("${app.jwtSecret}")
    private String jwtSecret;


    @PostConstruct
    public void  validateJwtSecret(){

        if (jwtSecret==null || jwtSecret.trim().isEmpty()){
            logger.error("La clave secreta JWT no está configurada correctamente. Por favor, verifica la configuración de tu aplicación.");
            throw new IllegalArgumentException("La clave secreta JWT no está configurada correctamente.");
        }
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);

        if (keyBytes.length < 32) {
            logger.error("La clave secreta JWT debe tener al menos 32 bytes de longitud.");
            throw new IllegalArgumentException("La clave secreta JWT debe tener al menos 32 bytes de longitud.");
        }

        if (keyBytes.length > 64) {
            logger.error("La clave secreta JWT no debe exceder los 64 bytes de longitud.");
            throw new IllegalArgumentException("La clave secreta JWT no debe exceder los 64 bytes de longitud.");
        }
    }

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs; // Almacena tiempo de expiracion tokens en milisegundos

    public String generateJwtToken(Authentication authentication) {

        UserDetails userPrincipal= (UserDetails) authentication.getPrincipal(); // Obtiene los detalles del usuario autenticado.
        return generateTokenFromUsername(userPrincipal.getUsername()); // Retorna el token generado.
    }


    public String generateTokenFromUsername(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public String getUserNameFromJwtToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Token JWT inválido: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Token JWT expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Token JWT no soportado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string está vacío: {}", e.getMessage());
        } catch (SignatureException e) {
            logger.error("Fallo en la firma JWT: {}", e.getMessage());
        }
        return false;
    }


    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUserNameFromJwtToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    public int getJwtExpirationMs() {
        return jwtExpirationMs;
    }

}
