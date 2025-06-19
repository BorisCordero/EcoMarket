package com.ecomarket.producto_service.Controller;

import com.ecomarket.producto_service.controller.ProductoController;
import com.ecomarket.producto_service.model.Producto;
import com.ecomarket.producto_service.service.ProductoService;

import org.junit.jupiter.api.Test;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Test
    void listarProductos_deberiaRetornarListaDeProductos() throws Exception {
        Producto p1 = new Producto(1, "Café", "Café orgánico", 3000.0, 10);
        Producto p2 = new Producto(2, "Té", "Té verde", 2500.0, 20);

        when(productoService.obtenerTodosLosProductos()).thenReturn(Arrays.asList(p1, p2));

        mockMvc.perform(get("/api/v1/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].nombreProducto", is("Café")));
    }

    @Test
    void obtenerProducto_existente() throws Exception {
        Producto producto = new Producto(1, "Café", "Café orgánico", 3000.0, 10);
        when(productoService.buscarProductoPorId(1)).thenReturn(Optional.of(producto));

        mockMvc.perform(get("/api/v1/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreProducto", is("Café")));
    }

    @Test
    void obtenerProducto_noExistente() throws Exception {
        when(productoService.buscarProductoPorId(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/productos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void registrarProducto_deberiaRetornarGuardado() throws Exception {
        Producto producto = new Producto(null, "Brownie", "Brownie vegano", 2000.0, 30);
        Producto guardado = new Producto(3, "Brownie", "Brownie vegano", 2000.0, 30);

        when(productoService.guardarProducto(any(Producto.class))).thenReturn(guardado);

        mockMvc.perform(post("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombreProducto\":\"Brownie\",\"descripcion\":\"Brownie vegano\",\"precioUnitario\":2000,\"stockDisponible\":30}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto", is(3)))
                .andExpect(jsonPath("$.nombreProducto", is("Brownie")));
    }

    @Test
    void eliminarProducto_deberiaRetornarNoContent() throws Exception {
        doNothing().when(productoService).eliminarProductoPorId(1);

        mockMvc.perform(delete("/api/v1/productos/1"))
                .andExpect(status().isNoContent());
    }
}
