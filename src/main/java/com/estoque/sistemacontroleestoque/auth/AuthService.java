package com.estoque.sistemacontroleestoque.auth;

import com.estoque.sistemacontroleestoque.auth.dto.LoginRequest;
import com.estoque.sistemacontroleestoque.auth.dto.LoginResponse;
import com.estoque.sistemacontroleestoque.auth.dto.RefreshTokenRequest;
import com.estoque.sistemacontroleestoque.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        var usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return LoginResponse.builder()
                .accessToken(jwtService.generateAccessToken(usuario))
                .refreshToken(jwtService.generateRefreshToken(usuario))
                .expiresIn(jwtService.getExpiration())
                .tokenType("Bearer")
                .build();
    }

    public LoginResponse refresh(RefreshTokenRequest request) {
        String username = jwtService.extractUsername(request.getRefreshToken());

        var usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!jwtService.isTokenValid(request.getRefreshToken(), usuario)) {
            throw new RuntimeException("Refresh token inválido ou expirado");
        }

        return LoginResponse.builder()
                .accessToken(jwtService.generateAccessToken(usuario))
                .refreshToken(jwtService.generateRefreshToken(usuario))
                .expiresIn(jwtService.getExpiration())
                .tokenType("Bearer")
                .build();
    }
}
