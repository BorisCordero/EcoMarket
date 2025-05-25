package com.ecomarket.cliente_service.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Cliente {
    private int idCliente;
    private String nombreCliente;
    private String emailCliente;
    private String direccionCliente;
}
