package com.ecomarket.producto_service.service;

import com.ecomarket.producto_service.model.Producto;
import com.ecomarket.producto_service.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> buscarProductoPorId(Integer idProducto) {
        return productoRepository.findById(idProducto);
    }

    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public void eliminarProductoPorId(Integer idProducto) {
        productoRepository.deleteById(idProducto);
    }
}
