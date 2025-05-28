package com.ecomarket.producto_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProducto;

    private String nombreProducto;
    private String descripcion;
    private Double precioUnitario;
    private Integer stockDisponible;
}
