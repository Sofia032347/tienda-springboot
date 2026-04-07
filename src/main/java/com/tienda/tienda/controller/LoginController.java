package com.tienda.tienda.controller;

import com.tienda.tienda.model.Usuario;
import com.tienda.tienda.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
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
    public String validarLogin(@ModelAttribute Usuario usuario, Model model, HttpSession session) {

        Optional<Usuario> user =
                repository.findByUsernameAndPassword(
                        usuario.getUsername(),
                        usuario.getPassword());

        if (user.isPresent()) {
            session.setAttribute("usuarioLogueado", user.get());
            return "redirect:/usuarios";
        } else {
            model.addAttribute("error", "Usuario o contraseña errados, intente de nuevo");
            return "login";
        }
    }

}