package com.api.libreria.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.api.libreria.models.PrestamoModel;

public interface PrestamoService {

    List<PrestamoModel> obtenerPrestamos();

    Optional<PrestamoModel> obtenerPrestamoPorId(Long id);

    PrestamoModel registrarPrestamo(Long isbn, Long socioId, LocalDate fechaLimite);

    PrestamoModel registrarDevolucion(Long id);

    boolean eliminarPrestamo(Long id);

}