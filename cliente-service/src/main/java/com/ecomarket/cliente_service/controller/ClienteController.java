package com.ecomarket.cliente_service.controller;

import com.ecomarket.cliente_service.model.Cliente;
import com.ecomarket.cliente_service.service.ClienteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Clientes", description = "Operaciones relacionadas con clientes")
@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(summary = "Listar todos los clientes")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Cliente>>> listarClientes() {
        List<Cliente> clientes = clienteService.obtenerTodosLosClientes();

        List<EntityModel<Cliente>> clientesConLinks = clientes.stream()
            .map(cliente -> EntityModel.of(cliente,
                    linkTo(methodOn(ClienteController.class).obtenerCliente(cliente.getIdCliente())).withSelfRel()
            )).collect(Collectors.toList());

        CollectionModel<EntityModel<Cliente>> collectionModel = CollectionModel.of(clientesConLinks,
            linkTo(methodOn(ClienteController.class).listarClientes()).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Obtener un cliente por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Cliente>> obtenerCliente(@PathVariable Integer id) {
        return clienteService.buscarClientePorId(id)
            .map(cliente -> EntityModel.of(cliente,
                    linkTo(methodOn(ClienteController.class).obtenerCliente(id)).withSelfRel(),
                    linkTo(methodOn(ClienteController.class).listarClientes()).withRel("todos-los-clientes")
            ))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Registrar un nuevo cliente")
    @PostMapping
    public ResponseEntity<Cliente> registrarCliente(@RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.guardarCliente(cliente));
    }

    @Operation(summary = "Actualizar un cliente existente por su ID")
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Integer id, @RequestBody Cliente clienteActualizado) {
        return clienteService.buscarClientePorId(id)
            .map(clienteExistente -> {
                clienteActualizado.setIdCliente(id);
                return ResponseEntity.ok(clienteService.guardarCliente(clienteActualizado));
            }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un cliente por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Integer id) {
        clienteService.eliminarClientePorId(id);
        return ResponseEntity.noContent().build();
    }
}
