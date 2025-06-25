package com.ecomarket.pedido_service.controller;

import com.ecomarket.pedido_service.model.Pedido;
import com.ecomarket.pedido_service.service.PedidoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Pedidos", description = "Operaciones relacionadas con pedidos")
@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(summary = "Listar todos los pedidos con enlaces HATEOAS")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> listarPedidos() {
        List<Pedido> pedidos = pedidoService.obtenerTodosLosPedidos();

        List<EntityModel<Pedido>> pedidosConLinks = pedidos.stream()
            .map(pedido -> EntityModel.of(pedido,
                linkTo(methodOn(PedidoController.class).obtenerPedido(pedido.getIdPedido())).withSelfRel(),
                linkTo(methodOn(PedidoController.class).listarPedidos()).withRel("todos-los-pedidos")
            ))
            .collect(Collectors.toList());

        CollectionModel<EntityModel<Pedido>> collectionModel = CollectionModel.of(
            pedidosConLinks,
            linkTo(methodOn(PedidoController.class).listarPedidos()).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Obtener un pedido por su ID con enlaces HATEOAS")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Pedido>> obtenerPedido(@PathVariable Integer id) {
        Optional<Pedido> pedidoOpt = pedidoService.buscarPedidoPorId(id);

        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();
            EntityModel<Pedido> recurso = EntityModel.of(pedido,
                linkTo(methodOn(PedidoController.class).obtenerPedido(id)).withSelfRel(),
                linkTo(methodOn(PedidoController.class).listarPedidos()).withRel("todos-los-pedidos")
            );
            return ResponseEntity.ok(recurso);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Registrar un nuevo pedido")
    @PostMapping
    public ResponseEntity<EntityModel<Pedido>> registrarPedido(@RequestBody Pedido pedido) {
        Pedido nuevoPedido = pedidoService.guardarPedido(pedido);
        EntityModel<Pedido> recurso = EntityModel.of(nuevoPedido,
            linkTo(methodOn(PedidoController.class).obtenerPedido(nuevoPedido.getIdPedido())).withSelfRel(),
            linkTo(methodOn(PedidoController.class).listarPedidos()).withRel("todos-los-pedidos")
        );
        return ResponseEntity.ok(recurso);
    }

    @Operation(summary = "Actualizar un pedido existente por su ID")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Pedido>> actualizarPedido(@PathVariable Integer id, @RequestBody Pedido pedidoActualizado) {
        Optional<Pedido> pedidoExistente = pedidoService.buscarPedidoPorId(id);

        if (pedidoExistente.isPresent()) {
            pedidoActualizado.setIdPedido(id);
            Pedido actualizado = pedidoService.guardarPedido(pedidoActualizado);
            EntityModel<Pedido> recurso = EntityModel.of(actualizado,
                linkTo(methodOn(PedidoController.class).obtenerPedido(id)).withSelfRel(),
                linkTo(methodOn(PedidoController.class).listarPedidos()).withRel("todos-los-pedidos")
            );
            return ResponseEntity.ok(recurso);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar un pedido por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Integer id) {
        pedidoService.eliminarPedidoPorId(id);
        return ResponseEntity.noContent().build();
    }
}
