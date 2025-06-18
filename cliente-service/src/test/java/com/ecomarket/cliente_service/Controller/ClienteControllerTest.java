package com.ecomarket.cliente_service.Controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ecomarket.cliente_service.model.Cliente;
import com.ecomarket.cliente_service.repository.ClienteRepository;
import com.ecomarket.cliente_service.service.ClienteService;

class ClienteControllerTest {

    private ClienteRepository clienteRepository;
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        clienteRepository = Mockito.mock(ClienteRepository.class);
        clienteService = new ClienteService(clienteRepository);
    }

    @Test
    void obtenerTodosLosClientes_deberiaRetornarLista() {
        Cliente cliente1 = new Cliente(1, "Juan", "juan@email.com", "Calle 123");
        Cliente cliente2 = new Cliente(2, "Ana", "ana@email.com", "Av. 456");

        when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente1, cliente2));

        List<Cliente> resultado = clienteService.obtenerTodosLosClientes();

        assertEquals(2, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombreCompleto());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void buscarClientePorId_existente_deberiaRetornarCliente() {
        Cliente cliente = new Cliente(1, "Pedro", "pedro@email.com", "Calle Falsa 123");
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));

        Optional<Cliente> resultado = clienteService.buscarClientePorId(1);

        assertTrue(resultado.isPresent());
        assertEquals("Pedro", resultado.get().getNombreCompleto());
        verify(clienteRepository).findById(1);
    }

    @Test
    void buscarClientePorId_noExistente_deberiaRetornarVacio() {
        when(clienteRepository.findById(99)).thenReturn(Optional.empty());

        Optional<Cliente> resultado = clienteService.buscarClientePorId(99);

        assertTrue(resultado.isEmpty());
        verify(clienteRepository).findById(99);
    }

    @Test
    void guardarCliente_deberiaRetornarClienteGuardado() {
        Cliente cliente = new Cliente(null, "Luis", "luis@email.com", "Pasaje 7");
        Cliente clienteGuardado = new Cliente(3, "Luis", "luis@email.com", "Pasaje 7");

        when(clienteRepository.save(cliente)).thenReturn(clienteGuardado);

        Cliente resultado = clienteService.guardarCliente(cliente);

        assertEquals(3, resultado.getIdCliente());
        verify(clienteRepository).save(cliente);
    }

    @Test
    void eliminarClientePorId_deberiaLlamarDeleteById() {
        clienteService.eliminarClientePorId(5);
        verify(clienteRepository, times(1)).deleteById(5);
    }
}

