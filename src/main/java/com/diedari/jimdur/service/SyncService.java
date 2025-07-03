package com.diedari.jimdur.service;

import com.diedari.jimdur.model.security.Usuario;
import com.diedari.jimdur.repository.security.UsuarioRepository;
import com.diedari.jimdur.repository.business.UsuarioRefRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para sincronizar datos entre la base de datos de seguridad y la de negocio.
 * Este servicio mantiene la consistencia entre ambas bases de datos.
 */
@Service
@Transactional
public class SyncService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioRefRepository usuarioRefRepository;

    /**
     * Sincroniza un usuario desde la base de datos de seguridad a la de negocio.
     * @param usuarioId ID del usuario a sincronizar
     */
    public void sincronizarUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + usuarioId));

        // En esta implementación híbrida, el usuario ya existe en ambas bases de datos
        // Solo verificamos que existe
        if (!usuarioRefRepository.existsById(usuarioId)) {
            throw new RuntimeException("Usuario no encontrado en base de datos de negocio: " + usuarioId);
        }
    }

    /**
     * Sincroniza todos los usuarios activos.
     */
    public void sincronizarTodosLosUsuarios() {
        usuarioRepository.findAll().forEach(usuario -> {
            try {
                sincronizarUsuario(usuario.getId());
                System.out.println("Usuario sincronizado: " + usuario.getEmail());
            } catch (Exception e) {
                System.err.println("Error sincronizando usuario " + usuario.getEmail() + ": " + e.getMessage());
            }
        });
    }

    /**
     * Verifica si un usuario existe en ambas bases de datos.
     * @param usuarioId ID del usuario a verificar
     * @return true si existe en ambas bases de datos
     */
    public boolean verificarConsistenciaUsuario(Long usuarioId) {
        boolean existeEnSeguridad = usuarioRepository.existsById(usuarioId);
        boolean existeEnNegocio = usuarioRefRepository.existsById(usuarioId);
        return existeEnSeguridad && existeEnNegocio;
    }
} 