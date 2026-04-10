package com.tienda.tienda.controller;

import com.tienda.tienda.model.Usuario;
import com.tienda.tienda.repository.UsuarioRepository;
import com.tienda.tienda.repository.ClienteRepository;
import com.tienda.tienda.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reportes")
public class ReportesController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping
    public String menuReportes() {
        return "reportes";
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        List<Usuario> lista = usuarioRepository.findAll();

        if (lista.isEmpty()) {
            model.addAttribute("mensaje", "No hay usuarios registrados");
        }

        model.addAttribute("listaUsuarios", lista);
        return "reporteUsuarios";
    }

    @GetMapping("/clientes")
    public String listarClientes(Model model) {
        model.addAttribute("listaClientes", clienteRepository.findAll());
        return "reporteClientes";
    }

    @GetMapping("/ventasporcliente")
    public String reporteVentas(Model model) {
        List<Map<String, Object>> ventas = ventaRepository.obtenerVentasPorCliente();

        double totalGeneral = ventas.stream()
                .mapToDouble(m -> ((Number) m.get("total")).doubleValue())
                .sum();

        model.addAttribute("reporteVentas", ventas);
        model.addAttribute("totalGeneral", totalGeneral);
        return "reporteVentasPorCliente";
    }
}