package com.api.libreria.controllers;

import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

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
