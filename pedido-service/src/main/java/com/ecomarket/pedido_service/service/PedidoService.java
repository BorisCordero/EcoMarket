package com.ecomarket.pedido_service.service;

import com.ecomarket.pedido_service.model.Pedido;
import com.ecomarket.pedido_service.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> obtenerTodosLosPedidos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> buscarPedidoPorId(Integer idPedido) {
        return pedidoRepository.findById(idPedido);
    }

    public Pedido guardarPedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public void eliminarPedidoPorId(Integer idPedido) {
        pedidoRepository.deleteById(idPedido);
    }
}
