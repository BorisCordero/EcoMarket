package com.ecomarket.auth_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Usuario {
    private String username;
    private String password;
    private String rol;
}
