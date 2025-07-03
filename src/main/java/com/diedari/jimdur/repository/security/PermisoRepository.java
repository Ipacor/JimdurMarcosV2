package com.diedari.jimdur.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diedari.jimdur.model.security.Permiso;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Long> {

} 