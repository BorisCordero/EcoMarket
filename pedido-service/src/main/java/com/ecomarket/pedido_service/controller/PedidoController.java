package com.ecomarket.pedido_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecomarket.pedido_service.model.Pedido;
import com.ecomarket.pedido_service.service.PedidoService;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedidos() {
        return ResponseEntity.ok(pedidoService.obtenerTodosLosPedidos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPedido(@PathVariable Integer id) {
        return pedidoService.buscarPedidoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pedido> registrarPedido(@RequestBody Pedido pedido) {
        return ResponseEntity.ok(pedidoService.guardarPedido(pedido));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> actualizarPedido(@PathVariable Integer id, @RequestBody Pedido pedidoActualizado) {
        return pedidoService.buscarPedidoPorId(id)
                .map(pedidoExistente -> {
                    pedidoActualizado.setIdPedido(id);
                    return ResponseEntity.ok(pedidoService.guardarPedido(pedidoActualizado));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Integer id) {
        pedidoService.eliminarPedidoPorId(id);
        return ResponseEntity.noContent().build();
    }
}
