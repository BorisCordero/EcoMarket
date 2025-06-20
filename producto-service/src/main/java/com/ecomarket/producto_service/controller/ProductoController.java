package com.ecomarket.producto_service.controller;

import com.ecomarket.producto_service.model.Producto;
import com.ecomarket.producto_service.service.ProductoService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> listarProductos() {
        List<Producto> productos = productoService.obtenerTodosLosProductos();

        List<EntityModel<Producto>> productosConLinks = productos.stream()
            .map(producto -> EntityModel.of(producto,
                linkTo(methodOn(ProductoController.class).obtenerProducto(producto.getIdProducto())).withSelfRel()
            )).collect(Collectors.toList());

        CollectionModel<EntityModel<Producto>> collectionModel = CollectionModel.of(productosConLinks,
            linkTo(methodOn(ProductoController.class).listarProductos()).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> obtenerProducto(@PathVariable Integer id) {
        return productoService.buscarProductoPorId(id)
            .map(producto -> EntityModel.of(producto,
                linkTo(methodOn(ProductoController.class).obtenerProducto(id)).withSelfRel(),
                linkTo(methodOn(ProductoController.class).listarProductos()).withRel("todos-los-productos")
            ))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Producto> registrarProducto(@RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.guardarProducto(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Integer id, @RequestBody Producto productoActualizado) {
        return productoService.buscarProductoPorId(id)
            .map(productoExistente -> {
                productoActualizado.setIdProducto(id);
                return ResponseEntity.ok(productoService.guardarProducto(productoActualizado));
            }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Integer id) {
        productoService.eliminarProductoPorId(id);
        return ResponseEntity.noContent().build();
    }
}
