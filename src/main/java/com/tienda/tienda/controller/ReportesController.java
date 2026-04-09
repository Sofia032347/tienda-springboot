package com.tienda.tienda.controller;

import com.tienda.tienda.model.Usuario;
import com.tienda.tienda.repository.UsuarioRepository;
import com.tienda.tienda.repository.ClienteRepository; // Asumo que tienes este
import com.tienda.tienda.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class ReportesController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/reportes")
    public String menuReportes() {
        return "reportes";
    }

    // HU-021: Listado de Usuarios
    @GetMapping("/consutayreportes/usuarios")
    public String listarUsuarios(Model model) {
        List<Usuario> lista = usuarioRepository.findAll();

        if(lista.isEmpty()) {
            model.addAttribute("mensaje", "No hay usuarios registrados");
        }

        // Usamos "listaUsuarios" para que coincida con tu HTML
        model.addAttribute("listaUsuarios", lista);

        // Verifica que tu archivo se llame exactamente reporteUsuarios.html (sin la u extra)
        return "reporteUsuarios";
    }

    // HU-022: Listado de Clientes
    @GetMapping("/consutayreportes/clientes")
    public String listarClientes(Model model) {
        model.addAttribute("listaClientes", clienteRepository.findAll());
        return "reporteClientes";
    }

    // HU-023: Ventas por Cliente
    @GetMapping("/consutayreportes/ventasporcliente")
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