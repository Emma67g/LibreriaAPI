package com.api.libreria.controllers;

import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

/** Convierte los errores de validación de un formulario en un solo texto para mostrar en la alerta. */
final class MensajesValidacion {

    private MensajesValidacion() {
    }

    static String de(BindingResult resultado) {
        return resultado.getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .distinct()
                .collect(Collectors.joining(" · "));
    }
}
