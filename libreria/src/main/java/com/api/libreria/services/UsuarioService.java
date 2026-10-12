package com.api.libreria.services;

import java.util.List;
import java.util.Optional;

import com.api.libreria.models.UsuarioModel;

public interface UsuarioService {

    List<UsuarioModel> obtenerUsuarios();

    Optional<UsuarioModel> obtenerUsuarioPorId(Long id);

    UsuarioModel guardarUsuario(UsuarioModel usuario);

    UsuarioModel actualizarUsuario(Long id, UsuarioModel usuario);

    boolean eliminarUsuario(Long id);

}