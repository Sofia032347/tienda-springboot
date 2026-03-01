package com.tienda.tienda.controller;

import com.tienda.tienda.entity.Usuario;
import com.tienda.tienda.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class LoginController {

    private final UsuarioRepository repository;

    public LoginController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    @PostMapping("/login")
    public String validarLogin(@ModelAttribute Usuario usuario, Model model) {

        Optional<Usuario> user =
                repository.findByUsernameAndPassword(
                        usuario.getUsername(),
                        usuario.getPassword());

        if (user.isPresent()) {
            return "redirect:/home";
        } else {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "login";
        }
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
}