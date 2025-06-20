package com.ecomarket.venta_service.Controller;
import com.ecomarket.venta_service.controller.VentaController;

import com.ecomarket.venta_service.model.Venta;
import com.ecomarket.venta_service.service.VentaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;


import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VentaController.class)
public class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VentaService ventaService;

    @Test
    void listarVentas_deberiaRetornarLista() throws Exception {
        Venta v1 = new Venta(1, 101, 5000.0, "Efectivo", LocalDate.of(2025, 6, 18));
        Venta v2 = new Venta(2, 102, 7500.0, "Transferencia", LocalDate.of(2025, 6, 17));

        when(ventaService.obtenerTodasLasVentas()).thenReturn(Arrays.asList(v1, v2));

        mockMvc.perform(get("/api/v1/ventas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$._embedded.ventaList[0].metodoPago", is("Efectivo")));

    }

    @Test
    void obtenerVenta_existente() throws Exception {
        Venta venta = new Venta(1, 101, 5000.0, "Efectivo", LocalDate.of(2025, 6, 18));
        when(ventaService.buscarVentaPorId(1)).thenReturn(Optional.of(venta));

        mockMvc.perform(get("/api/v1/ventas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.montoTotal", is(5000.0)));
    }

    @Test
    void obtenerVenta_noExistente() throws Exception {
        when(ventaService.buscarVentaPorId(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/ventas/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void registrarVenta_deberiaRetornarGuardado() throws Exception {
        Venta venta = new Venta(null, 101, 8500.0, "Tarjeta", LocalDate.of(2025, 6, 18));
        Venta guardada = new Venta(3, 101, 8500.0, "Tarjeta", LocalDate.of(2025, 6, 18));

        when(ventaService.registrarVenta(any(Venta.class))).thenReturn(guardada);

        mockMvc.perform(post("/api/v1/ventas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"idCliente\":101,\"montoTotal\":8500,\"metodoPago\":\"Tarjeta\",\"fechaVenta\":\"2025-06-18\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idVenta", is(3)))
                .andExpect(jsonPath("$.metodoPago", is("Tarjeta")));
    }

    @Test
    void eliminarVenta_deberiaRetornarNoContent() throws Exception {
        doNothing().when(ventaService).eliminarVentaPorId(1);

        mockMvc.perform(delete("/api/v1/ventas/1"))
                .andExpect(status().isNoContent());
    }
}
