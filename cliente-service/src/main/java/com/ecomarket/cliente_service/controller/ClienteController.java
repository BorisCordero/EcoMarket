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
import java.util.Optional;
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

    @Operation(summary = "Listar todos los clientes con enlaces HATEOAS")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Cliente>>> listarClientes() {
        List<Cliente> clientes = clienteService.obtenerTodosLosClientes();

        List<EntityModel<Cliente>> clientesConLinks = clientes.stream()
            .map(cliente -> EntityModel.of(cliente,
                linkTo(methodOn(ClienteController.class).obtenerCliente(cliente.getIdCliente())).withSelfRel(),
                linkTo(methodOn(ClienteController.class).listarClientes()).withRel("todos-los-clientes")
            ))
            .collect(Collectors.toList());

        CollectionModel<EntityModel<Cliente>> collectionModel = CollectionModel.of(
            clientesConLinks,
            linkTo(methodOn(ClienteController.class).listarClientes()).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Obtener un cliente por su ID con enlaces HATEOAS")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Cliente>> obtenerCliente(@PathVariable Integer id) {
        Optional<Cliente> clienteOpt = clienteService.buscarClientePorId(id);

        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            EntityModel<Cliente> recurso = EntityModel.of(cliente,
                linkTo(methodOn(ClienteController.class).obtenerCliente(id)).withSelfRel(),
                linkTo(methodOn(ClienteController.class).listarClientes()).withRel("todos-los-clientes")
            );
            return ResponseEntity.ok(recurso);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Registrar un nuevo cliente")
    @PostMapping
    public ResponseEntity<EntityModel<Cliente>> registrarCliente(@RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.guardarCliente(cliente);
        EntityModel<Cliente> recurso = EntityModel.of(nuevoCliente,
            linkTo(methodOn(ClienteController.class).obtenerCliente(nuevoCliente.getIdCliente())).withSelfRel(),
            linkTo(methodOn(ClienteController.class).listarClientes()).withRel("todos-los-clientes")
        );
        return ResponseEntity.ok(recurso);
    }

    @Operation(summary = "Actualizar un cliente existente por su ID")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Cliente>> actualizarCliente(@PathVariable Integer id, @RequestBody Cliente clienteActualizado) {
        Optional<Cliente> clienteExistente = clienteService.buscarClientePorId(id);

        if (clienteExistente.isPresent()) {
            clienteActualizado.setIdCliente(id);
            Cliente actualizado = clienteService.guardarCliente(clienteActualizado);
            EntityModel<Cliente> recurso = EntityModel.of(actualizado,
                linkTo(methodOn(ClienteController.class).obtenerCliente(id)).withSelfRel(),
                linkTo(methodOn(ClienteController.class).listarClientes()).withRel("todos-los-clientes")
            );
            return ResponseEntity.ok(recurso);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar un cliente por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Integer id) {
        clienteService.eliminarClientePorId(id);
        return ResponseEntity.noContent().build();
    }
}
