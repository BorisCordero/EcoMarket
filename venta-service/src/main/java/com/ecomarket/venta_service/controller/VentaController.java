package com.ecomarket.venta_service.controller;

import com.ecomarket.venta_service.model.Venta;
import com.ecomarket.venta_service.service.VentaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Ventas", description = "Operaciones relacionadas con ventas")
@RestController
@RequestMapping("/api/v1/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @Operation(summary = "Listar todas las ventas")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Venta>>> listarVentas() {
        List<Venta> ventas = ventaService.obtenerTodasLasVentas();

        List<EntityModel<Venta>> ventasConLinks = ventas.stream()
            .map(venta -> EntityModel.of(venta,
                linkTo(methodOn(VentaController.class).obtenerVenta(venta.getIdVenta())).withSelfRel()
            )).collect(Collectors.toList());

        CollectionModel<EntityModel<Venta>> collectionModel = CollectionModel.of(ventasConLinks,
            linkTo(methodOn(VentaController.class).listarVentas()).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Obtener una venta por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Venta>> obtenerVenta(@PathVariable Integer id) {
        return ventaService.buscarVentaPorId(id)
            .map(venta -> EntityModel.of(venta,
                linkTo(methodOn(VentaController.class).obtenerVenta(id)).withSelfRel(),
                linkTo(methodOn(VentaController.class).listarVentas()).withRel("todas-las-ventas")
            ))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Registrar una nueva venta")
    @PostMapping
    public ResponseEntity<Venta> registrarVenta(@RequestBody Venta nuevaVenta) {
        return ResponseEntity.ok(ventaService.registrarVenta(nuevaVenta));
    }

    @Operation(summary = "Actualizar una venta existente por su ID")
    @PutMapping("/{id}")
    public ResponseEntity<Venta> actualizarVenta(@PathVariable Integer id, @RequestBody Venta ventaActualizada) {
        return ResponseEntity.ok(ventaService.actualizarVenta(id, ventaActualizada));
    }

    @Operation(summary = "Eliminar una venta por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVenta(@PathVariable Integer id) {
        ventaService.eliminarVentaPorId(id);
        return ResponseEntity.noContent().build();
    }
}
