package com.ecomarket.pedido_service.Controller;

import com.ecomarket.pedido_service.controller.PedidoController;
import com.ecomarket.pedido_service.model.Pedido;
import com.ecomarket.pedido_service.service.PedidoService;
import com.ecomarket.pedido_service.config.JwtFilter;
import com.ecomarket.pedido_service.util.JwtUtil;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(PedidoController.class)
@Import(PedidoControllerTest.Config.class)
class PedidoControllerTest {

    @TestConfiguration
    static class Config {
        @Bean
        public JwtUtil jwtUtil() {
            return org.mockito.Mockito.mock(JwtUtil.class);
        }

        @Bean
        public JwtFilter jwtFilter(JwtUtil jwtUtil) {
            return new JwtFilter(jwtUtil);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @Test
    void listarPedidos_deberiaRetornarListaDePedidos() throws Exception {
        Pedido pedido1 = new Pedido(1, 101, "Café", 2, "pendiente");
        Pedido pedido2 = new Pedido(2, 102, "Té", 1, "enviado");

        when(pedidoService.obtenerTodosLosPedidos()).thenReturn(Arrays.asList(pedido1, pedido2));

        mockMvc.perform(get("/api/v1/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.pedidoList.length()", is(2)))
                .andExpect(jsonPath("$._embedded.pedidoList[0].nombreProducto").value("Café"));
    }

    @Test
    void obtenerPedido_existente_deberiaRetornarPedido() throws Exception {
        Pedido pedido = new Pedido(1, 101, "Café", 2, "pendiente");
        when(pedidoService.buscarPedidoPorId(1)).thenReturn(Optional.of(pedido));

        mockMvc.perform(get("/api/v1/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreProducto", is("Café")));
    }

    @Test
    void obtenerPedido_noExistente_deberiaRetornar404() throws Exception {
        when(pedidoService.buscarPedidoPorId(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/pedidos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void registrarPedido_deberiaRetornarPedidoGuardado() throws Exception {
        Pedido pedido = new Pedido(null, 101, "Café", 2, "pendiente");
        Pedido guardado = new Pedido(1, 101, "Café", 2, "pendiente");

        when(pedidoService.guardarPedido(any(Pedido.class))).thenReturn(guardado);

        mockMvc.perform(post("/api/v1/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"idCliente\":101,\"nombreProducto\":\"Café\",\"cantidadSolicitada\":2,\"estado\":\"pendiente\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPedido", is(1)))
                .andExpect(jsonPath("$.nombreProducto", is("Café")));
    }

    @Test
    void eliminarPedido_deberiaRetornarNoContent() throws Exception {
        doNothing().when(pedidoService).eliminarPedidoPorId(1);

        mockMvc.perform(delete("/api/v1/pedidos/1"))
                .andExpect(status().isNoContent());
    }
}
