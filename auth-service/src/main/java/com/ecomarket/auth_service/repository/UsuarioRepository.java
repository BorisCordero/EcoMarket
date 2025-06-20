package com.ecomarket.auth_service.repository;

import com.ecomarket.auth_service.model.Usuario;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UsuarioRepository {

    private final List<Usuario> usuarios = List.of(
        new Usuario("admin", "admin123", "ADMIN"),
        new Usuario("user", "user123", "USER")
    );

    public Optional<Usuario> findByUsername(String username) {
        return usuarios.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }
}
