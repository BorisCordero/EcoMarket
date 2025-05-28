package com.ecomarket.cliente_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cliente") // Nombre de la tabla en ecomarket_db
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer idCliente;

    @Column(name = "nombre_completo")
    private String nombreCompleto;

    @Column(name = "correo_electronico")
    private String correoElectronico;

    @Column(name = "direccion")
    private String direccion;
}
