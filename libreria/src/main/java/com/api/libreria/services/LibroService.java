package com.api.libreria.services;

import java.util.List;
import java.util.Optional;

import com.api.libreria.models.LibroModel;

public interface LibroService {

    List<LibroModel> obtenerLibros();

    Optional<LibroModel> obtenerLibroPorIsbn(Long isbn);

    LibroModel guardarLibro(LibroModel libro);

    LibroModel actualizarLibro(Long isbn, LibroModel libro);

    boolean eliminarLibro(Long isbn);

}