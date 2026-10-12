package com.api.libreria.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.api.libreria.models.UsuarioModel;
import com.api.libreria.repositories.IPrestamoRepository;
import com.api.libreria.repositories.IUsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final IUsuarioRepository usuarioRepository;
    private final IPrestamoRepository prestamoRepository;

    public UsuarioServiceImpl(IUsuarioRepository usuarioRepository, IPrestamoRepository prestamoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.prestamoRepository = prestamoRepository;
    }

    @Override
    public List<UsuarioModel> obtenerUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<UsuarioModel> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public UsuarioModel guardarUsuario(UsuarioModel usuario) {
        if (usuario.getContrasena() == null || usuario.getContrasena().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        usuario.setId(null); 
        usuario.setFechacreacion(LocalDate.now());
        return usuarioRepository.save(usuario);
    }

    @Override
    public UsuarioModel actualizarUsuario(Long id, UsuarioModel usuario) {
        UsuarioModel existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el usuario con id " + id));
        usuario.setId(id);
        usuario.setFechacreacion(existente.getFechacreacion());
        if (usuario.getContrasena() == null || usuario.getContrasena().isBlank()) {
            usuario.setContrasena(existente.getContrasena());
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public boolean eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            return false;
        }
        if (prestamoRepository.existsBySocio_Id(id)) {
            throw new IllegalStateException(
                    "No se puede eliminar el usuario porque tiene préstamos asociados. Elimina primero esos préstamos.");
        }
        usuarioRepository.deleteById(id);
        return true;
    }

}
