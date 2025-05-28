package com.ecomarket.pedido_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPedido;

    private Integer idCliente;
    private String nombreProducto;
    private Integer cantidadSolicitada;
    private String estado; // ej: "Pendiente", "Despachado", etc.
}
