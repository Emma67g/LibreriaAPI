package com.api.libreria.controllers;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.libreria.models.PrestamoModel;
import com.api.libreria.services.PrestamoService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    public record PrestamoRequest(
            @NotNull(message = "El id del libro es obligatorio") Long isbn,
            @NotNull(message = "El id del socio es obligatorio") Long socioId,
            LocalDate fechaLimite) {
    }

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @GetMapping
    public List<PrestamoModel> obtenerTodos() {
        return prestamoService.obtenerPrestamos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrestamoModel> obtenerPorId(@PathVariable Long id) {
        return prestamoService.obtenerPrestamoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PrestamoModel> crear(@Valid @RequestBody PrestamoRequest solicitud) {
        PrestamoModel creado = prestamoService.registrarPrestamo(
                solicitud.isbn(), solicitud.socioId(), solicitud.fechaLimite());
        return ResponseEntity.created(URI.create("/api/prestamos/" + creado.getId())).body(creado);
    }

    @PostMapping("/{id}/devolver")
    public ResponseEntity<PrestamoModel> devolver(@PathVariable Long id) {
        if (prestamoService.obtenerPrestamoPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(prestamoService.registrarDevolucion(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!prestamoService.eliminarPrestamo(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

}