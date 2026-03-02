package com.tienda.tienda.controller;

import com.tienda.tienda.model.Usuario;
import com.tienda.tienda.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Redirección inicial del sistema
    @GetMapping("/")
    public String inicio() {
        return "redirect:/login";
    }

    // Listar usuarios
    @GetMapping("/usuarios")
    public String listarUsuarios(@RequestParam(required = false) String buscar, Model model) {

        List<Usuario> listaUsuarios;

        if (buscar != null && !buscar.isEmpty()) {
            listaUsuarios = usuarioRepository
                    .findByNombreCompletoContainingIgnoreCaseOrUsernameContainingIgnoreCaseOrCedulaContainingIgnoreCase(
                            buscar, buscar, buscar);
        } else {
            listaUsuarios = usuarioRepository.findAll();
        }

        model.addAttribute("listaUsuarios", listaUsuarios);
        model.addAttribute("buscar", buscar);

        return "usuarios";
    }

    // Mostrar formulario nuevo
    @GetMapping("/usuarios/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "formUsuario";
    }

    // Guardar usuario
    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario) {
        usuarioRepository.save(usuario);
        return "redirect:/usuarios";
    }

    // Editar usuario
    @GetMapping("/usuarios/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        model.addAttribute("usuario", usuario);
        return "formUsuario";
    }

    // Eliminar usuario
    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
        return "redirect:/usuarios";
    }
}