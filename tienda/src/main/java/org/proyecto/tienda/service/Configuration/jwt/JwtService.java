package org.proyecto.tienda.service.Configuration.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.proyecto.tienda.configuration.User.UserUtils;
import org.proyecto.tienda.entity.Client.Usuario;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250655368566D59713373";
    public String getToken(Usuario usuario) {
        UserUtils usuarioUtils = new UserUtils(usuario);
        return getToken(new HashMap<>(), usuarioUtils);
    }
    
    private String getToken(Map<String, Object> extraClaims, UserUtils usuarioUtils) {
        return Jwts.builder()
            .claims(extraClaims)
            .claim("userId", usuarioUtils.getId())
            .claim("correo", usuarioUtils.getUsername())
            .claim("role", usuarioUtils.getAuthorities())
            .subject(usuarioUtils.getUsername())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis()+1000L*60*60*24))
            .signWith(getKey())
            .compact();
    }

    private SecretKey getKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException e) {
            return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        }
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String usuario = getUsernameFromToken(token);
        return (usuario.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private Claims getAllClaims(String token){
        return Jwts.parser()
        .verifyWith(getKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token){
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){
        return getExpiration(token).before(new Date());
    }
}
