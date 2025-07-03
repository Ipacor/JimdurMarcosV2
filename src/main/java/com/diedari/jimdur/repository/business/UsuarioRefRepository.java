package com.diedari.jimdur.repository.business;

import com.diedari.jimdur.model.business.UsuarioRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRefRepository extends JpaRepository<UsuarioRef, Long> {
    
    Optional<UsuarioRef> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    Optional<UsuarioRef> findByEmailAndEstadoCuenta(String email, String estadoCuenta);
} 