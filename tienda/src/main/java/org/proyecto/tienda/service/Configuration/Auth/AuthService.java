package org.proyecto.tienda.service.Configuration.Auth;

import org.proyecto.tienda.entity.Client.Usuario;
import org.proyecto.tienda.model.Enum.Access.Role;
import org.proyecto.tienda.model.Request.Auth.Login.LoginRequest;
import org.proyecto.tienda.model.Request.Auth.Register.RegisterRequest;
import org.proyecto.tienda.model.response.Auth.AuthResponse;
import org.proyecto.tienda.repository.Client.UsuarioRepo;
import org.proyecto.tienda.service.Configuration.jwt.JwtService;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepo usuarioRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public @Nullable AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        Usuario usuario = usuarioRepo.findByEmail(request.getEmail()).orElseThrow();
        String jwtToken = jwtService.getToken(usuario);
        return AuthResponse.builder()
        .token(jwtToken)
        .build();
    }

    public @Nullable AuthResponse register(RegisterRequest request) {
        Usuario usuario = Usuario.builder()
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USUARIO)
                .build();
        usuarioRepo.save(usuario);
        return AuthResponse.builder().token(jwtService.getToken(usuario)).build();
    }

}
