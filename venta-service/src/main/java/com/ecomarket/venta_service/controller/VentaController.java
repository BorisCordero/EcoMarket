package com.ecomarket.venta_service.controller;

import com.ecomarket.venta_service.model.Venta;
import com.ecomarket.venta_service.service.VentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    public ResponseEntity<List<Venta>> listarVentas() {
        return ResponseEntity.ok(ventaService.obtenerTodasLasVentas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerVenta(@PathVariable Integer id) {
        return ventaService.buscarVentaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Venta> registrarVenta(@RequestBody Venta nuevaVenta) {
        return ResponseEntity.ok(ventaService.registrarVenta(nuevaVenta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venta> actualizarVenta(@PathVariable Integer id, @RequestBody Venta ventaActualizada) {
        return ResponseEntity.ok(ventaService.actualizarVenta(id, ventaActualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVenta(@PathVariable Integer id) {
        ventaService.eliminarVentaPorId(id);
        return ResponseEntity.noContent().build();
    }
}
