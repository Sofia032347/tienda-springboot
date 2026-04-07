package com.tienda.tienda.controller;

import com.tienda.tienda.model.Proveedor;
import com.tienda.tienda.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
            if(listaProveedores.isEmpty()){
                model.addAttribute("error","Proveedor Inexistente");
            }
        } else {
            listaProveedores = proveedorRepository.findAll();
        }

        model.addAttribute("listaProveedores", listaProveedores);
        model.addAttribute("buscar", buscar);

        return "proveedores";
    }

    // NUEVO PROVEEDOR
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("proveedor", new Proveedor());
        return "formProveedor";
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardarProveedor(@ModelAttribute Proveedor proveedor,
                                   RedirectAttributes redirectAttributes) {

        boolean esNuevo = (proveedor.getId() == null);

        if (proveedor.getNit().isEmpty() ||
                proveedor.getNombreProveedor().isEmpty() ||
                proveedor.getDireccion().isEmpty() ||
                proveedor.getTelefono().isEmpty() ||
                proveedor.getCiudad().isEmpty()) {

            redirectAttributes.addFlashAttribute("error",
                    "Faltan datos del proveedor");

            if (esNuevo) {
                redirectAttributes.addFlashAttribute("error",
                        "Faltan datos del proveedor");
                return "redirect:/proveedores/nuevo";
            } else {
                redirectAttributes.addFlashAttribute("error",
                    "Datos faltantes");
                return "redirect:/proveedores/editar/" + proveedor.getId();
            }
        }

        proveedorRepository.save(proveedor);

        redirectAttributes.addFlashAttribute("mensaje",
                esNuevo ? "Proveedor Creado" :
                        "Datos del Proveedor Actualizados");

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
    public String eliminarProveedor(@PathVariable Long id,
                                    RedirectAttributes redirectAttributes) {

        if(proveedorRepository.existsById(id)){
            proveedorRepository.deleteById(id);

            redirectAttributes.addFlashAttribute("mensaje",
                    "Datos del Proveedor Borrados");

        }else{
            redirectAttributes.addFlashAttribute("error",
                    "Proveedor Inexistente");
        }

        return "redirect:/proveedores";
    }
}