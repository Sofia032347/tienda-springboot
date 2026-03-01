/*package com.tienda.tienda.controller;

import com.tienda.tienda.entity.Usuario;
import com.tienda.tienda.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository repository;

    public UsuarioController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("listaUsuarios", repository.findAll());
        return "usuarios";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "formUsuario";
    }

    @PostMapping("/guardar")
    public String guardar(Usuario usuario) {
        repository.save(usuario);
        return "redirect:/usuarios";
    }
}*/