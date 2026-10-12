package com.api.libreria.controllers;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.libreria.services.LibroService;
import com.api.libreria.services.PrestamoService;
import com.api.libreria.services.UsuarioService;

@Controller
public class PrestamoVistaController {

    private final PrestamoService prestamoService;
    private final LibroService libroService;
    private final UsuarioService usuarioService;

    public PrestamoVistaController(PrestamoService prestamoService, LibroService libroService,
            UsuarioService usuarioService) {
        this.prestamoService = prestamoService;
        this.libroService = libroService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/vista/prestamos")
    public String mostrarPrestamos(Model model) {
        model.addAttribute("prestamos", prestamoService.obtenerPrestamos());
        model.addAttribute("librosDisponibles",
                libroService.obtenerLibros().stream().filter(l -> l.getNumejemplares() > 0).toList());
        model.addAttribute("socios",
                usuarioService.obtenerUsuarios().stream().filter(u -> "ACTIVO".equalsIgnoreCase(u.getEstado())).toList());
        return "prestamos";
    }

    @PostMapping("/vista/prestamos/guardar")
    public String guardarPrestamo(@RequestParam Long isbn, @RequestParam Long socioId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaLimite,
            RedirectAttributes redirectAttributes) {
        try {
            prestamoService.registrarPrestamo(isbn, socioId, fechaLimite);
            redirectAttributes.addFlashAttribute("mensaje", "Préstamo registrado correctamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/vista/prestamos";
    }

    @PostMapping("/vista/prestamos/{id}/devolver")
    public String devolverPrestamo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (prestamoService.obtenerPrestamoPorId(id).isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "No se encontró el préstamo con id " + id);
        } else {
            prestamoService.registrarDevolucion(id);
            redirectAttributes.addFlashAttribute("mensaje", "Devolución registrada correctamente");
        }
        return "redirect:/vista/prestamos";
    }

    @PostMapping("/vista/prestamos/{id}/eliminar")
    public String eliminarPrestamo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (prestamoService.eliminarPrestamo(id)) {
            redirectAttributes.addFlashAttribute("mensaje", "Préstamo eliminado correctamente");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se encontró el préstamo con id " + id);
        }
        return "redirect:/vista/prestamos";
    }

}