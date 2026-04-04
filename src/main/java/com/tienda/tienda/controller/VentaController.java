package com.tienda.tienda.controller;

import com.tienda.tienda.model.Venta;
import com.tienda.tienda.repository.VentaRepository;
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

    // 🔹 LISTAR VENTAS
    @GetMapping
    public String listarVentas(Model model) {

        List<Venta> listaVentas = ventaRepository.findAll();

        model.addAttribute("listaVentas", listaVentas); // 🔴 IMPORTANTE

        return "ventas";
    }

    // 🔹 FORMULARIO NUEVA VENTA
    @GetMapping("/nuevo")
    public String nuevaVenta(Model model) {

        model.addAttribute("venta", new Venta());

        return "formVenta";
    }

    // 🔹 GUARDAR VENTA
    @PostMapping("/guardar")
    public String guardarVenta(@ModelAttribute Venta venta) {

        // 🔥 Cálculo automático
        double iva = venta.getTotalVenta() * 0.19;
        venta.setTotalIva(iva);
        venta.setTotalConIva(venta.getTotalVenta() + iva);

        ventaRepository.save(venta);

        return "redirect:/ventas";
    }

    // 🔹 ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminarVenta(@PathVariable Long id) {

        ventaRepository.deleteById(id);

        return "redirect:/ventas";
    }
}