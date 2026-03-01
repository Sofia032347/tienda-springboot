package com.tienda.tienda.controller;

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
        model.addAttribute("usuarios", repository.findAll());
        return "usuarios";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "formUsuario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Usuario usuario) {

        if (usuario.getId() != null) {
            Usuario usuarioExistente = repository.findById(usuario.getId()).orElse(null);

            if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
                usuario.setPassword(usuarioExistente.getPassword());
            }
        }

        repository.save(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Usuario usuario = repository.findById(id).orElse(null);
        model.addAttribute("usuario", usuario);
        return "formUsuario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/usuarios";
    }
}