package com.tienda.tienda.controller;

import com.tienda.tienda.model.Proveedor;
import com.tienda.tienda.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorRepository proveedorRepository;

    // LISTAR + BUSCAR
    @GetMapping
    public String listarProveedores(@RequestParam(required = false) String buscar, Model model) {

        List<Proveedor> listaProveedores;

        if (buscar != null && !buscar.isEmpty()) {
            listaProveedores = proveedorRepository
                    .findByNombreProveedorContainingIgnoreCaseOrNitContainingIgnoreCaseOrCiudadContainingIgnoreCase(
                            buscar, buscar, buscar);
        } else {
            listaProveedores = proveedorRepository.findAll();
        }

        model.addAttribute("listaProveedores", listaProveedores);
        model.addAttribute("buscar", buscar);

        return "proveedores";
    }

    // FORM NUEVO
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("proveedor", new Proveedor());
        return "formProveedor";
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardarProveedor(@ModelAttribute Proveedor proveedor) {
        proveedorRepository.save(proveedor);
        return "redirect:/proveedores";
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editarProveedor(@PathVariable Long id, Model model) {
        Proveedor proveedor = proveedorRepository.findById(id).orElse(null);
        model.addAttribute("proveedor", proveedor);
        return "formProveedor";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminarProveedor(@PathVariable Long id) {
        proveedorRepository.deleteById(id);
        return "redirect:/proveedores";
    }
}