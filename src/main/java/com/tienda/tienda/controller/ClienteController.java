package com.tienda.tienda.controller;

import com.tienda.tienda.model.Cliente;
import com.tienda.tienda.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    // LISTAR + BUSCAR
    @GetMapping
    public String listarClientes(@RequestParam(required = false) String buscar, Model model) {

        List<Cliente> listaClientes;

        if (buscar != null && !buscar.isEmpty()) {
            listaClientes = clienteRepository
                    .findByNombreCompletoContainingIgnoreCaseOrCedulaContainingIgnoreCase(
                            buscar, buscar);
            if(listaClientes.isEmpty()){
                model.addAttribute("error","Cliente Inexistente");
            }
        } else {
            listaClientes = clienteRepository.findAll();
        }

        model.addAttribute("listaClientes", listaClientes);
        model.addAttribute("buscar", buscar);

        return "clientes";
    }

    // FORM NUEVO
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "formCliente";
    }

    // GUARDAR (crear y actualizar)
    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute Cliente cliente,
                                 RedirectAttributes redirectAttributes) {

        boolean esNuevo = (cliente.getId() == null);

        if (cliente.getCedula().isEmpty() ||
                cliente.getNombreCompleto().isEmpty() ||
                cliente.getDireccion().isEmpty() ||
                cliente.getTelefono().isEmpty() ||
                cliente.getCorreoElectronico().isEmpty()) {

            if (esNuevo) {
                redirectAttributes.addFlashAttribute("error", "Faltan datos del cliente");
                return "redirect:/clientes/nuevo";
            } else {
                redirectAttributes.addFlashAttribute("error", "Datos faltantes");
                return "redirect:/clientes/editar/" + cliente.getId();
            }
        }

        clienteRepository.save(cliente);

        redirectAttributes.addFlashAttribute("mensaje",
                esNuevo ? "Cliente Creado" : "Datos del Cliente Actualizados");

        return "redirect:/clientes";
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable Long id, Model model) {
        Cliente cliente = clienteRepository.findById(id).orElse(null);
        model.addAttribute("cliente", cliente);
        return "formCliente";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id,
                                  RedirectAttributes redirectAttributes) {

        if(clienteRepository.existsById(id)){
            clienteRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("mensaje",
                    "Datos del Cliente Borrados");
        } else {
            redirectAttributes.addFlashAttribute("error",
                    "Cliente Inexistente");
        }

        return "redirect:/clientes";
    }
}
