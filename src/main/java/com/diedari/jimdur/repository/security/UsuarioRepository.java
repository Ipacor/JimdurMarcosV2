package com.diedari.jimdur.repository.security;

import com.diedari.jimdur.model.security.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    Optional<Usuario> findByEmailAndEstadoCuenta(String email, String estadoCuenta);
    
    boolean existsByEmailAndIdNot(String email, Long id);
} 