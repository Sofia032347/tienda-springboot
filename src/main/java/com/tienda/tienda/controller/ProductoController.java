package com.tienda.tienda.controller;

import com.tienda.tienda.model.Producto;
import com.tienda.tienda.repository.ProductoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoRepository repository;

    public ProductoController(ProductoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("listaProductos", repository.findAll());
        return "productos"; // nombre del HTML
    }

    @GetMapping("/nuevo")
    public String nuevoProducto(Model model) {
        model.addAttribute("producto", new Producto());
        return "formProducto";
    }

    @PostMapping("/guardar")
    public String guardar(Producto producto) {
        repository.save(producto);
        return "redirect:/productos";
    }
}