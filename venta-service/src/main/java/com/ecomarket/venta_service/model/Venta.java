package com.ecomarket.venta_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idVenta;

    private Integer idCliente;
    private Double montoTotal;
    private String metodoPago; // Ej: "Tarjeta", "Transferencia", "Efectivo"
    private LocalDate fechaVenta;
}
