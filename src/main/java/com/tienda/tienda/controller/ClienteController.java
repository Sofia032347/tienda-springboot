package com.tienda.tienda.controller;

import com.tienda.tienda.model.Cliente;
import com.tienda.tienda.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String guardarCliente(@ModelAttribute Cliente cliente) {
        clienteRepository.save(cliente);
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
    public String eliminarCliente(@PathVariable Long id) {
        clienteRepository.deleteById(id);
        return "redirect:/clientes";
    }
}
