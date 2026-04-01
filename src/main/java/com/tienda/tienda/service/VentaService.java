package com.tienda.tienda.service;

import com.tienda.tienda.model.*;
import com.tienda.tienda.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private DetalleVentaRepository detalleRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // 🔥 Registrar venta (Sprint 4 completo)
    public String registrarVenta(String cedulaCliente, List<DetalleVenta> detalles) {

        // Validar cliente
        Cliente cliente = clienteRepository.findByCedula(cedulaCliente);
        if (cliente == null) {
            return "Cliente no existe";
        }

        // Máx 3 productos
        if (detalles.size() > 3) {
            return "Máximo 3 productos";
        }

        double totalVenta = 0;

        for (DetalleVenta d : detalles) {

            Producto producto = productoRepository.findByCodigoProducto(d.getCodigoProducto());

            if (producto == null) {
                return "Producto no existe";
            }

            if (d.getCantidad() <= 0) {
                return "Cantidad inválida";
            }

            double totalProducto = d.getCantidad() * producto.getPrecioVenta();

            d.setValorUnitario(producto.getPrecioVenta());
            d.setTotalProducto(totalProducto);

            totalVenta += totalProducto;
        }

        double iva = totalVenta * 0.19;
        double totalConIva = totalVenta + iva;

        Long consecutivo = ventaRepository.count() + 1;

        Venta venta = new Venta();
        venta.setCodigoVenta(consecutivo);
        venta.setCedulaCliente(cedulaCliente);
        venta.setTotalVenta(totalVenta);
        venta.setTotalIva(iva);
        venta.setTotalConIva(totalConIva);

        ventaRepository.save(venta);

        for (DetalleVenta d : detalles) {
            d.setCodigoVenta(consecutivo);
            detalleRepository.save(d);
        }

        return "Venta registrada correctamente";
    }

    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    public List<DetalleVenta> obtenerDetalle(Long codigoVenta) {
        return detalleRepository.findByCodigoVenta(codigoVenta);
    }
}