package com.ecomarket.auth_service.service;

import com.ecomarket.auth_service.model.AuthRequest;
import com.ecomarket.auth_service.model.AuthResponse;
import com.ecomarket.auth_service.model.Usuario;
import com.ecomarket.auth_service.repository.UsuarioRepository;
import com.ecomarket.auth_service.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse login(AuthRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtUtil.generateToken(usuario.getUsername(), usuario.getRol());
        return new AuthResponse(token);
    }
}
