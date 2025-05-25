package com.ecomarket.cliente_service.service;

import com.ecomarket.cliente_service.model.Cliente;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final List<Cliente> listaClientes = new ArrayList<>();
    private int idSecuencialCliente = 1;

    public List<Cliente> obtenerTodosLosClientes() {
        return listaClientes;
    }

    public Cliente registrarCliente(Cliente nuevoCliente) {
        nuevoCliente.setIdCliente(idSecuencialCliente++);
        listaClientes.add(nuevoCliente);
        return nuevoCliente;
    }

    public Optional<Cliente> buscarClientePorId(int idCliente) {
        return listaClientes.stream()
                .filter(cliente -> cliente.getIdCliente() == idCliente)
                .findFirst();
    }

    public Cliente actualizarCliente(int idCliente, Cliente datosActualizados) {
        Cliente clienteExistente = buscarClientePorId(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        clienteExistente.setNombreCliente(datosActualizados.getNombreCliente());
        clienteExistente.setEmailCliente(datosActualizados.getEmailCliente());
        clienteExistente.setDireccionCliente(datosActualizados.getDireccionCliente());
        return clienteExistente;
    }

    public void eliminarCliente(int idCliente) {
        Cliente clienteAEliminar = buscarClientePorId(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        listaClientes.remove(clienteAEliminar);
    }
}
