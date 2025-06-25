package com.ecomarket.producto_service.controller;

import com.ecomarket.producto_service.model.Producto;
import com.ecomarket.producto_service.service.ProductoService;
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

@Tag(name = "Productos", description = "Operaciones relacionadas con productos")
@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @Operation(summary = "Listar todos los productos")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> listarProductos() {
        List<Producto> productos = productoService.obtenerTodosLosProductos();

        List<EntityModel<Producto>> productosConLinks = productos.stream()
            .map(producto -> EntityModel.of(producto,
                linkTo(methodOn(ProductoController.class).obtenerProducto(producto.getIdProducto())).withSelfRel(),
                linkTo(methodOn(ProductoController.class).listarProductos()).withRel("todos-los-productos")
            ))
            .collect(Collectors.toList());

        CollectionModel<EntityModel<Producto>> collectionModel = CollectionModel.of(
            productosConLinks,
            linkTo(methodOn(ProductoController.class).listarProductos()).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Obtener un producto por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> obtenerProducto(@PathVariable Integer id) {
        Optional<Producto> productoOpt = productoService.buscarProductoPorId(id);

        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            EntityModel<Producto> recurso = EntityModel.of(producto,
                linkTo(methodOn(ProductoController.class).obtenerProducto(id)).withSelfRel(),
                linkTo(methodOn(ProductoController.class).listarProductos()).withRel("todos-los-productos")
            );
            return ResponseEntity.ok(recurso);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Registrar un nuevo producto")
    @PostMapping
    public ResponseEntity<EntityModel<Producto>> registrarProducto(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.guardarProducto(producto);
        EntityModel<Producto> recurso = EntityModel.of(nuevoProducto,
            linkTo(methodOn(ProductoController.class).obtenerProducto(nuevoProducto.getIdProducto())).withSelfRel(),
            linkTo(methodOn(ProductoController.class).listarProductos()).withRel("todos-los-productos")
        );
        return ResponseEntity.ok(recurso);
    }

    @Operation(summary = "Actualizar un producto existente por su ID")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> actualizarProducto(@PathVariable Integer id, @RequestBody Producto productoActualizado) {
        Optional<Producto> productoExistente = productoService.buscarProductoPorId(id);

        if (productoExistente.isPresent()) {
            productoActualizado.setIdProducto(id);
            Producto actualizado = productoService.guardarProducto(productoActualizado);
            EntityModel<Producto> recurso = EntityModel.of(actualizado,
                linkTo(methodOn(ProductoController.class).obtenerProducto(id)).withSelfRel(),
                linkTo(methodOn(ProductoController.class).listarProductos()).withRel("todos-los-productos")
            );
            return ResponseEntity.ok(recurso);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar un producto por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Integer id) {
        productoService.eliminarProductoPorId(id);
        return ResponseEntity.noContent().build();
    }
}
