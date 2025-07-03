package com.diedari.jimdur.service;

import com.diedari.jimdur.model.security.Usuario;
import com.diedari.jimdur.repository.security.UsuarioRepository;
import com.diedari.jimdur.repository.business.UsuarioRefRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para migrar datos existentes entre las bases de datos.
 * Este servicio se usa para la migración inicial de datos.
 */
@Service
@Transactional
public class DataMigrationService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioRefRepository usuarioRefRepository;

    @Autowired
    private SyncService syncService;

    /**
     * Migra todos los usuarios existentes de la base de datos original
     * a las nuevas bases de datos separadas.
     */
    public void migrarUsuariosExistentes() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        
        for (Usuario usuario : usuarios) {
            try {
                syncService.sincronizarUsuario(usuario.getId());
                System.out.println("Usuario migrado: " + usuario.getEmail());
            } catch (Exception e) {
                System.err.println("Error migrando usuario " + usuario.getEmail() + ": " + e.getMessage());
            }
        }
        
        System.out.println("Migración completada. Total usuarios procesados: " + usuarios.size());
    }

    /**
     * Verifica la consistencia de datos entre las bases de datos.
     */
    public void verificarConsistencia() {
        long usuariosSeguridad = usuarioRepository.count();
        long usuariosNegocio = usuarioRefRepository.count();
        
        System.out.println("Usuarios en base de datos de seguridad: " + usuariosSeguridad);
        System.out.println("Usuarios en base de datos de negocio: " + usuariosNegocio);
        
        if (usuariosSeguridad == usuariosNegocio) {
            System.out.println("✅ Consistencia verificada correctamente");
        } else {
            System.out.println("❌ Inconsistencia detectada");
        }
    }
} 