package com.tienda.tienda.controller;

import com.tienda.tienda.model.DetalleVenta;
import com.tienda.tienda.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    // 🟢 Mostrar formulario
    @GetMapping("/nuevo")
    public String mostrarFormulario() {
        return "formVenta";
    }

    // 🔵 Guardar venta
    @PostMapping("/guardar")
    public String guardarVenta(

            @RequestParam String cedulaCliente,

            @RequestParam(required = false) Long codigoProducto1,
            @RequestParam(required = false) Integer cantidad1,

            @RequestParam(required = false) Long codigoProducto2,
            @RequestParam(required = false) Integer cantidad2,

            @RequestParam(required = false) Long codigoProducto3,
            @RequestParam(required = false) Integer cantidad3,

            RedirectAttributes redirectAttributes
    ) {

        List<DetalleVenta> detalles = new ArrayList<>();

        // 📦 Producto 1
        if (codigoProducto1 != null && cantidad1 != null) {
            DetalleVenta d1 = new DetalleVenta();
            d1.setCodigoProducto(codigoProducto1);
            d1.setCantidad(cantidad1);
            detalles.add(d1);
        }

        // 📦 Producto 2
        if (codigoProducto2 != null && cantidad2 != null) {
            DetalleVenta d2 = new DetalleVenta();
            d2.setCodigoProducto(codigoProducto2);
            d2.setCantidad(cantidad2);
            detalles.add(d2);
        }

        // 📦 Producto 3
        if (codigoProducto3 != null && cantidad3 != null) {
            DetalleVenta d3 = new DetalleVenta();
            d3.setCodigoProducto(codigoProducto3);
            d3.setCantidad(cantidad3);
            detalles.add(d3);
        }

        // 🔴 Validación básica (opcional pero recomendada)
        if (detalles.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "Debe agregar al menos un producto");
            return "redirect:/ventas/nuevo";
        }






        // ⚙️ Llamar al service (aquí está TODA la lógica del Sprint 4)
        String resultado = ventaService.registrarVenta(cedulaCliente, detalles);

        // 📢 Mensaje al usuario
        redirectAttributes.addFlashAttribute("mensaje", resultado);

        return "redirect:/ventas/nuevo";
    }
}
