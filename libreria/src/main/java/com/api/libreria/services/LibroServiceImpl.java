package com.api.libreria.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.api.libreria.models.LibroModel;
import com.api.libreria.repositories.ILibroRepository;
import com.api.libreria.repositories.IPrestamoRepository;

@Service
public class LibroServiceImpl implements LibroService {

    private final ILibroRepository libroRepository;
    private final IPrestamoRepository prestamoRepository;

    public LibroServiceImpl(ILibroRepository libroRepository, IPrestamoRepository prestamoRepository) {
        this.libroRepository = libroRepository;
        this.prestamoRepository = prestamoRepository;
    }

    @Override
    public List<LibroModel> obtenerLibros() {
        return libroRepository.findAll();
    }

    @Override
    public Optional<LibroModel> obtenerLibroPorIsbn(Long isbn) {
        return libroRepository.findById(isbn);
    }

    @Override
    public LibroModel guardarLibro(LibroModel libro) {
        libro.setIsbn(null); // un alta nunca debe sobrescribir un libro existente
        return libroRepository.save(libro);
    }

    @Override
    public LibroModel actualizarLibro(Long isbn, LibroModel libro) {
        libro.setIsbn(isbn);
        return libroRepository.save(libro);
    }

    @Override
    public boolean eliminarLibro(Long isbn) {
        if (!libroRepository.existsById(isbn)) {
            return false;
        }
        if (prestamoRepository.existsByLibro_Isbn(isbn)) {
            throw new IllegalStateException(
                    "No se puede eliminar el libro porque tiene préstamos asociados. Elimina primero esos préstamos.");
        }
        libroRepository.deleteById(isbn);
        return true;
    }

}
