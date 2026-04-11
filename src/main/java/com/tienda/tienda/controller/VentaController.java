package com.tienda.tienda.controller;

import com.tienda.tienda.model.*;
import com.tienda.tienda.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    // LISTAR VENTAS
    @GetMapping
    public String listarVentas(Model model) {

        List<Venta> listaVentas = ventaRepository.findAll();

        for (Venta v : listaVentas) {

            // Muestra nombre USUARIO
            Usuario user = usuarioRepository.findByCedula(v.getCedulaUsuario()).orElse(null);
            if (user != null) {
                v.setNombreUsuario(user.getNombreCompleto());
            }

            // Muestra nombre CLIENTE
            Cliente cliente = clienteRepository.findByCedula(v.getCedulaCliente()).orElse(null);
            if (cliente != null) {
                v.setNombreCliente(cliente.getNombreCompleto());
            }
        }

        model.addAttribute("listaVentas", listaVentas);

        return "ventas";
    }

    // NUEVA VENTA
    @GetMapping("/nuevo")
    public String nuevaVenta(

            @RequestParam(required = false) String cedula,

            @RequestParam(required = false) Long codigoProducto1,
            @RequestParam(required = false) Integer cantidad1,

            @RequestParam(required = false) Long codigoProducto2,
            @RequestParam(required = false) Integer cantidad2,

            @RequestParam(required = false) Long codigoProducto3,
            @RequestParam(required = false) Integer cantidad3,

            Model model) {

        // CLIENTE
        if (cedula != null) {
            Cliente cliente = clienteRepository.findByCedula(cedula).orElse(null);

            if (cliente != null) {
                model.addAttribute("cliente", cliente);
                model.addAttribute("mensajeCliente", "Cliente encontrado");
            } else {
                model.addAttribute("errorCliente", "Cliente no encontrado");
            }
        }

        // PRODUCTO 1
        double total1 = 0;
        if (codigoProducto1 != null) {
            Producto producto1 = productoRepository.findByCodigoProducto(codigoProducto1).orElse(null);

            if (producto1 != null) {
                int cant = (cantidad1 != null) ? cantidad1 : 1;
                total1 = cant * producto1.getPrecioVenta();

                model.addAttribute("producto1", producto1);
                model.addAttribute("cantidad1", cant);
                model.addAttribute("total1", total1);
                model.addAttribute("mensajeProducto1", "Producto encontrado");
            } else {
                model.addAttribute("errorProducto1", "Producto no encontrado");
            }
        }

        // PRODUCTO 2
        double total2 = 0;
        if (codigoProducto2 != null) {
            Producto producto2 = productoRepository.findByCodigoProducto(codigoProducto2).orElse(null);

            if (producto2 != null) {
                int cant = (cantidad2 != null) ? cantidad2 : 1;
                total2 = cant * producto2.getPrecioVenta();

                model.addAttribute("producto2", producto2);
                model.addAttribute("cantidad2", cant);
                model.addAttribute("total2", total2);
                model.addAttribute("mensajeProducto2", "Producto encontrado");
            } else {
                model.addAttribute("errorProducto2", "Producto no encontrado");
            }
        }

        // PRODUCTO 3
        double total3 = 0;
        if (codigoProducto3 != null) {
            Producto producto3 = productoRepository.findByCodigoProducto(codigoProducto3).orElse(null);

            if (producto3 != null) {
                int cant = (cantidad3 != null) ? cantidad3 : 1;
                total3 = cant * producto3.getPrecioVenta();

                model.addAttribute("producto3", producto3);
                model.addAttribute("cantidad3", cant);
                model.addAttribute("total3", total3);
                model.addAttribute("mensajeProducto3", "Producto encontrado");
            } else {
                model.addAttribute("errorProducto3", "Producto no encontrado");
            }
        }

        // TOTALES
        double totalVenta = total1 + total2 + total3;
        double totalIVA = totalVenta * 0.19;
        double totalConIVA = totalVenta + totalIVA;

        model.addAttribute("totalVenta", totalVenta);
        model.addAttribute("totalIVA", totalIVA);
        model.addAttribute("totalConIVA", totalConIVA);

        return "formVenta";
    }

    // GUARDAR VENTA
    @PostMapping("/guardar")
    public String guardarVenta(

            @RequestParam String cedula,

            @RequestParam(required = false) Long codigoProducto1,
            @RequestParam(required = false) Integer cantidad1,

            @RequestParam(required = false) Long codigoProducto2,
            @RequestParam(required = false) Integer cantidad2,

            @RequestParam(required = false) Long codigoProducto3,
            @RequestParam(required = false) Integer cantidad3,

            HttpSession session
    ) {

        // OBTENER USUARIO LOGUEADO
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            return "redirect:/login";
        }

        Cliente cliente = clienteRepository.findByCedula(cedula).orElse(null);

        if (cliente == null) {
            return "redirect:/ventas/nuevo?errorCliente=1";
        }

        double total1 = 0;
        double total2 = 0;
        double total3 = 0;

        // VALIDACIÓN DE CANTIDAD (sin perder datos)
        if ((cantidad1 != null && cantidad1 <= 0) ||
                (cantidad2 != null && cantidad2 <= 0) ||
                (cantidad3 != null && cantidad3 <= 0)) {

            return "redirect:/ventas/nuevo"
                    + "?cedula=" + cedula
                    + "&codigoProducto1=" + (codigoProducto1 != null ? codigoProducto1 : "")
                    + "&cantidad1=" + (cantidad1 != null ? cantidad1 : "")
                    + "&codigoProducto2=" + (codigoProducto2 != null ? codigoProducto2 : "")
                    + "&cantidad2=" + (cantidad2 != null ? cantidad2 : "")
                    + "&codigoProducto3=" + (codigoProducto3 != null ? codigoProducto3 : "")
                    + "&cantidad3=" + (cantidad3 != null ? cantidad3 : "")
                    + "&errorCantidad=La cantidad no puede ser menor o igual a 0";
        }

        if (codigoProducto1 != null && cantidad1 != null && cantidad1 > 0) {
            Producto p1 = productoRepository.findByCodigoProducto(codigoProducto1).orElse(null);
            if (p1 != null) {
                total1 = cantidad1 * p1.getPrecioVenta();
            }
        }

        if (codigoProducto2 != null && cantidad2 != null && cantidad2 > 0) {
            Producto p2 = productoRepository.findByCodigoProducto(codigoProducto2).orElse(null);
            if (p2 != null) {
                total2 = cantidad2 * p2.getPrecioVenta();
            }
        }

        if (codigoProducto3 != null && cantidad3 != null && cantidad3 > 0) {
            Producto p3 = productoRepository.findByCodigoProducto(codigoProducto3).orElse(null);
            if (p3 != null) {
                total3 = cantidad3 * p3.getPrecioVenta();
            }
        }

        double totalVenta = total1 + total2 + total3;
        double totalIVA = totalVenta * 0.19;
        double totalConIVA = totalVenta + totalIVA;

        Venta venta = new Venta();
        venta.setCedulaCliente(cedula);

        venta.setCedulaUsuario(usuario.getCedula());

        venta.setTotalVenta(totalVenta);
        venta.setTotalIva(totalIVA);
        venta.setTotalConIva(totalConIVA);

        Venta ventaGuardada = ventaRepository.save(venta);

        // PRODUCTO 1
        if (codigoProducto1 != null && cantidad1 != null && cantidad1 > 0) {
            Producto p1 = productoRepository.findByCodigoProducto(codigoProducto1).orElse(null);

            if (p1 != null) {
                DetalleVenta d1 = new DetalleVenta();
                d1.setCodigoVenta(ventaGuardada.getCodigoVenta());
                d1.setCodigoProducto(codigoProducto1);
                d1.setCantidad(cantidad1);
                d1.setValorUnitario(p1.getPrecioVenta());
                d1.setTotalProducto(cantidad1 * p1.getPrecioVenta());

                detalleVentaRepository.save(d1);
            }
        }

        // PRODUCTO 2
        if (codigoProducto2 != null && cantidad2 != null && cantidad2 > 0) {
            Producto p2 = productoRepository.findByCodigoProducto(codigoProducto2).orElse(null);

            if (p2 != null) {
                DetalleVenta d2 = new DetalleVenta();
                d2.setCodigoVenta(ventaGuardada.getCodigoVenta());
                d2.setCodigoProducto(codigoProducto2);
                d2.setCantidad(cantidad2);
                d2.setValorUnitario(p2.getPrecioVenta());
                d2.setTotalProducto(cantidad2 * p2.getPrecioVenta());

                detalleVentaRepository.save(d2);
            }
        }

        // PRODUCTO 3
        if (codigoProducto3 != null && cantidad3 != null && cantidad3 > 0) {
            Producto p3 = productoRepository.findByCodigoProducto(codigoProducto3).orElse(null);

            if (p3 != null) {
                DetalleVenta d3 = new DetalleVenta();
                d3.setCodigoVenta(ventaGuardada.getCodigoVenta());
                d3.setCodigoProducto(codigoProducto3);
                d3.setCantidad(cantidad3);
                d3.setValorUnitario(p3.getPrecioVenta());
                d3.setTotalProducto(cantidad3 * p3.getPrecioVenta());

                detalleVentaRepository.save(d3);
            }
        }

        return "redirect:/ventas";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminarVenta(@PathVariable Long id) {

        ventaRepository.deleteById(id);

        return "redirect:/ventas";
    }

    @GetMapping("/detalleventa/{id}")
    public String verDetalleVenta(@PathVariable Long id, Model model) {

        Venta venta = ventaRepository.findById(id).orElse(null);

        List<DetalleVenta> detalles = detalleVentaRepository.findByCodigoVenta(id);

        model.addAttribute("venta", venta);
        model.addAttribute("detalles", detalles);

        return "detalleventa";
    }

}