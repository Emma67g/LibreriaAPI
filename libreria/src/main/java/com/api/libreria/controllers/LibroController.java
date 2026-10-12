package com.api.libreria.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.libreria.models.LibroModel;
import com.api.libreria.services.LibroService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping
    public List<LibroModel> obtenerTodos() {
        return libroService.obtenerLibros();
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<LibroModel> obtenerPorId(@PathVariable Long isbn) {
        return libroService.obtenerLibroPorIsbn(isbn)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LibroModel> crear(@Valid @RequestBody LibroModel libro) {
        LibroModel creado = libroService.guardarLibro(libro);
        return ResponseEntity.created(URI.create("/api/libros/" + creado.getIsbn())).body(creado);
    }

    @PutMapping("/{isbn}")
    public ResponseEntity<LibroModel> actualizar(@PathVariable Long isbn, @Valid @RequestBody LibroModel libro) {
        if (libroService.obtenerLibroPorIsbn(isbn).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(libroService.actualizarLibro(isbn, libro));
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<Void> eliminar(@PathVariable Long isbn) {
        if (!libroService.eliminarLibro(isbn)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

}