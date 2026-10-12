package com.api.libreria.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.libreria.models.LibroModel;
import com.api.libreria.models.PrestamoModel;
import com.api.libreria.models.UsuarioModel;
import com.api.libreria.repositories.ILibroRepository;
import com.api.libreria.repositories.IPrestamoRepository;
import com.api.libreria.repositories.IUsuarioRepository;

@Service
public class PrestamoServiceImpl implements PrestamoService {

    private static final int MAX_PRESTAMOS_ACTIVOS = 3;
    private static final String ESTADO_ACTIVO = "ACTIVO";

    private final IPrestamoRepository prestamoRepository;
    private final ILibroRepository libroRepository;
    private final IUsuarioRepository usuarioRepository;

    public PrestamoServiceImpl(IPrestamoRepository prestamoRepository, ILibroRepository libroRepository,
            IUsuarioRepository usuarioRepository) {
        this.prestamoRepository = prestamoRepository;
        this.libroRepository = libroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<PrestamoModel> obtenerPrestamos() {
        return prestamoRepository.findAll();
    }

    @Override
    public Optional<PrestamoModel> obtenerPrestamoPorId(Long id) {
        return prestamoRepository.findById(id);
    }

    @Override
    @Transactional
    public PrestamoModel registrarPrestamo(Long isbn, Long socioId, LocalDate fechaLimite) {
        LibroModel libro = libroRepository.findById(isbn)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el libro con ISBN " + isbn));
        UsuarioModel socio = usuarioRepository.findById(socioId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el socio con id " + socioId));

        if (!ESTADO_ACTIVO.equalsIgnoreCase(socio.getEstado())) {
            throw new IllegalStateException("El socio " + socio.getNombre() + " " + socio.getApellidos()
                    + " está inactivo y no puede solicitar préstamos");
        }

        if (fechaLimite != null && fechaLimite.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha límite no puede ser anterior a hoy");
        }

        long prestamosActivos = prestamoRepository.countBySocio_IdAndFechaDevolucionIsNull(socioId);
        if (prestamosActivos >= MAX_PRESTAMOS_ACTIVOS) {
            throw new IllegalStateException(
                    "Este socio ya tiene " + prestamosActivos + " préstamos activos (máximo " + MAX_PRESTAMOS_ACTIVOS + ")");
        }

        if (libroRepository.descontarEjemplar(isbn) == 0) {
            throw new IllegalStateException("No hay ejemplares disponibles de \"" + libro.getTitulo() + "\"");
        }

        PrestamoModel prestamo = new PrestamoModel();
        prestamo.setLibro(libro);
        prestamo.setSocio(socio);
        if (fechaLimite != null) {
            prestamo.setFechaLimite(fechaLimite.atTime(PrestamoModel.FIN_DEL_DIA));
        }
        return prestamoRepository.save(prestamo);
    }

    @Override
    @Transactional
    public PrestamoModel registrarDevolucion(Long id) {
        PrestamoModel prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el préstamo con id " + id));

        if (prestamo.getFechaDevolucion() == null) {
            prestamo.setFechaDevolucion(LocalDateTime.now());
            libroRepository.reponerEjemplar(prestamo.getLibro().getIsbn());
            prestamoRepository.save(prestamo);
        }
        return prestamo;
    }

    @Override
    @Transactional
    public boolean eliminarPrestamo(Long id) {
        Optional<PrestamoModel> encontrado = prestamoRepository.findById(id);
        if (encontrado.isEmpty()) {
            return false;
        }
        PrestamoModel prestamo = encontrado.get();
        if (prestamo.getFechaDevolucion() == null) {
            libroRepository.reponerEjemplar(prestamo.getLibro().getIsbn());
        }
        prestamoRepository.delete(prestamo);
        return true;
    }

}
