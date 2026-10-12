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

import com.api.libreria.models.LibroModel;
import com.api.libreria.services.LibroService;

import jakarta.validation.Valid;

@Controller
public class LibroVistaController {

    private final LibroService libroService;

    public LibroVistaController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping({ "/", "/vista/libros" })
    public String mostrarLibros(Model model) {
        model.addAttribute("libros", libroService.obtenerLibros());
        if (!model.containsAttribute("libro")) {
            model.addAttribute("libro", new LibroModel());
        }
        return "libros";
    }

    @PostMapping("/vista/libros/guardar")
    public String guardarLibro(@Valid @ModelAttribute LibroModel libro, BindingResult resultado,
            RedirectAttributes atributos) {
        if (resultado.hasErrors()) {
            atributos.addFlashAttribute("error", MensajesValidacion.de(resultado));
            return "redirect:/vista/libros";
        }
        try {
            libroService.guardarLibro(libro);
            atributos.addFlashAttribute("mensaje", "Libro registrado correctamente.");
        } catch (DataIntegrityViolationException e) {
            atributos.addFlashAttribute("error", "Ya existe un libro con ese ISBN.");
        }
        return "redirect:/vista/libros";
    }

    @PostMapping("/vista/libros/{isbn}/actualizar")
    public String actualizarLibro(@PathVariable Long isbn, @Valid @ModelAttribute LibroModel libro,
            BindingResult resultado, RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", MensajesValidacion.de(resultado));
            return "redirect:/vista/libros";
        }
        if (libroService.obtenerLibroPorIsbn(isbn).isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "No se encontró el libro con ISBN " + isbn);
            return "redirect:/vista/libros";
        }
        try {
            libroService.actualizarLibro(isbn, libro);
            redirectAttributes.addFlashAttribute("mensaje", "Libro actualizado correctamente");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "Ya existe otro libro con ese ISBN.");
        }
        return "redirect:/vista/libros";
    }

    @PostMapping("/vista/libros/{isbn}/eliminar")
    public String eliminarLibro(@PathVariable Long isbn, RedirectAttributes redirectAttributes) {
        try {
            if (libroService.eliminarLibro(isbn)) {
                redirectAttributes.addFlashAttribute("mensaje", "Libro eliminado correctamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "No se encontró el libro con ISBN " + isbn);
            }
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/vista/libros";
    }

}
