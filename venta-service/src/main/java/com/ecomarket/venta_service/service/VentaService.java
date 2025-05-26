package com.ecomarket.venta_service.service;

import com.ecomarket.venta_service.model.Venta;
import com.ecomarket.venta_service.repository.VentaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;

    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    public List<Venta> obtenerTodasLasVentas() {
        return ventaRepository.findAll();
    }

    public Optional<Venta> buscarVentaPorId(Integer idVenta) {
        return ventaRepository.findById(idVenta);
    }

    public Venta registrarVenta(Venta nuevaVenta) {
        nuevaVenta.setFechaVenta(LocalDate.now());
        return ventaRepository.save(nuevaVenta);
    }

    public Venta actualizarVenta(Integer idVenta, Venta datosActualizados) {
        Venta ventaExistente = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        datosActualizados.setIdVenta(idVenta);
        return ventaRepository.save(datosActualizados);
    }

    public void eliminarVentaPorId(Integer idVenta) {
        ventaRepository.deleteById(idVenta);
    }
}
