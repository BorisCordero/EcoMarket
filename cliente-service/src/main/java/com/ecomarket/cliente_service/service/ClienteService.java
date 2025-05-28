package com.ecomarket.cliente_service.service;

import com.ecomarket.cliente_service.model.Cliente;
import com.ecomarket.cliente_service.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarClientePorId(Integer idCliente) {
        return clienteRepository.findById(idCliente);
    }

    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public void eliminarClientePorId(Integer idCliente) {
        clienteRepository.deleteById(idCliente);
    }
}
