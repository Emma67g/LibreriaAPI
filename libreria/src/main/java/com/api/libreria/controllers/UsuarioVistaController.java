package com.api.libreria.controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.libreria.models.UsuarioModel;
import com.api.libreria.services.UsuarioService;

import jakarta.validation.Valid;

@Controller
public class UsuarioVistaController {

    private final UsuarioService usuarioService;

    public UsuarioVistaController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/vista/usuarios")
    public String mostrarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.obtenerUsuarios());
        if (!model.containsAttribute("usuario")) {
            model.addAttribute("usuario", new UsuarioModel());
        }
        return "usuarios";
    }

    @PostMapping("/vista/usuarios/guardar")
    public String guardarUsuario(@Valid @ModelAttribute UsuarioModel usuario, BindingResult resultado,
            RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", MensajesValidacion.de(resultado));
            return "redirect:/vista/usuarios";
        }
        try {
            usuarioService.guardarUsuario(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario registrado correctamente");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "Ya existe un usuario con ese correo electrónico.");
        }
        return "redirect:/vista/usuarios";
    }

    @PostMapping("/vista/usuarios/{id}/actualizar")
    public String actualizarUsuario(@PathVariable Long id, @Valid @ModelAttribute UsuarioModel usuario,
            BindingResult resultado, RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", MensajesValidacion.de(resultado));
            return "redirect:/vista/usuarios";
        }
        try {
            usuarioService.actualizarUsuario(id, usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado correctamente");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "Ya existe otro usuario con ese correo electrónico.");
        }
        return "redirect:/vista/usuarios";
    }

    @PostMapping("/vista/usuarios/{id}/eliminar")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            if (usuarioService.eliminarUsuario(id)) {
                redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado correctamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "No se encontró el usuario con id " + id);
            }
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/vista/usuarios";
    }

}