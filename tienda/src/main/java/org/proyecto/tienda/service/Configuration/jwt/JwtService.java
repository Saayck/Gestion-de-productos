package org.proyecto.tienda.service.Configuration.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.proyecto.tienda.configuration.User.UserUtils;
import org.proyecto.tienda.entity.Client.Usuario;
import org.proyecto.tienda.model.Enum.Access.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String defaultSecret;

    @Value("${jwt.admin:${jwt.secret}}")
    private String adminSecret;

    @Value("${jwt.usuario:${jwt.secret}}")
    private String userSecret;

    public String getToken(Usuario usuario) {
        return getToken(new HashMap<>(), usuario);
    }

    private String getToken(Map<String, Object> extraClaims, Usuario usuario) {
        UserUtils usuarioUtils = new UserUtils(usuario);
        return Jwts.builder()
                .claims(extraClaims)
                .claim("userId", usuarioUtils.getId())
                .claim("correo", usuarioUtils.getUsername())
                .claim("role", usuario.getRole().name())
                .subject(usuarioUtils.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24))
                .signWith(getKeyForRole(usuario.getRole()))
                .compact();
    }

    private SecretKey getKeyForRole(Role role) {
        if (role == null) {
            return decodeSecret(defaultSecret);
        }

        return switch (role) {
            case ADMIN -> decodeSecret(adminSecret);
            case USUARIO -> decodeSecret(userSecret);
        };
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String usuario = getUsernameFromToken(token);
        return (usuario.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private Claims getAllClaims(String token) {
        for (SecretKey key : getAllSigningKeys()) {
            Claims claims = tryParseClaims(token, key);
            if (claims != null) {
                return claims;
            }
        }
        throw new IllegalArgumentException("Token signature validation failed for all known roles");
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token) {
        Claims claims = getAllClaims(token);
        return claims.getExpiration();
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    private SecretKey decodeSecret(String secret) {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secret);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException e) {
            return Keys.hmacShaKeyFor(secret.getBytes());
        }
    }

    private SecretKey getKeyForUserDetails(UserDetails userDetails) {
        Optional<? extends GrantedAuthority> authority = userDetails.getAuthorities()
                .stream()
                .findFirst();

        Role role = authority
                .map(GrantedAuthority::getAuthority)
                .map(this::resolveRole)
                .orElse(null);

        return getKeyForRole(role);
    }

    private Role resolveRole(String authority) {
        try {
            return Role.valueOf(authority);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private List<SecretKey> getAllSigningKeys() {
        return List.of(
                decodeSecret(adminSecret),
                decodeSecret(userSecret),
                decodeSecret(defaultSecret));
    }

    private Claims getAllClaims(String token, SecretKey key) {
        Claims claims = tryParseClaims(token, key);
        if (claims == null) {
            throw new IllegalArgumentException("Token signature validation failed for provided key");
        }
        return claims;
    }

    private Claims tryParseClaims(String token, SecretKey key) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            return null;
        }
    }
}
