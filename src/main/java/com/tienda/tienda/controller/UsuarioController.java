package com.tienda.tienda.controller;

import com.tienda.tienda.model.Usuario;
import com.tienda.tienda.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

            if (listaUsuarios.isEmpty()) {
                model.addAttribute("error", "Usuario Inexistente");
            }

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
    public String guardarUsuario(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {

        boolean esNuevo = (usuario.getId() == null);

        // Validación campos obligatorios
        if (usuario.getCedula() == null || usuario.getCedula().isEmpty() ||
                usuario.getNombreCompleto() == null || usuario.getNombreCompleto().isEmpty() ||
                usuario.getCorreo() == null || usuario.getCorreo().isEmpty() ||
                usuario.getUsername() == null || usuario.getUsername().isEmpty() ||
                (esNuevo && (usuario.getPassword() == null || usuario.getPassword().isEmpty()))) {

            if (esNuevo) {
                redirectAttributes.addFlashAttribute("error", "Faltan datos del usuario");
                return "redirect:/usuarios/nuevo";
            } else {
                redirectAttributes.addFlashAttribute("error", "Datos faltantes");
                return "redirect:/usuarios/editar/" + usuario.getId();
            }
        }

        // SI ES EDICIÓN Y LA CONTRASEÑA VIENE VACÍA, CONSERVAR LA ANTERIOR
        if (!esNuevo && (usuario.getPassword() == null || usuario.getPassword().isEmpty())) {

            Usuario usuarioBD = usuarioRepository.findById(usuario.getId()).orElse(null);

            if (usuarioBD != null) {
                usuario.setPassword(usuarioBD.getPassword());
            }
        }

        usuarioRepository.save(usuario);

        redirectAttributes.addFlashAttribute("mensaje",
                esNuevo ? "Usuario Creado" : "Datos del Usuario Actualizados");

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
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("mensaje", "Datos del Usuario Borrados");
        } else {
            redirectAttributes.addFlashAttribute("error", "Usuario Inexistente");
        }

        return "redirect:/usuarios";
    }
}