package com.diedari.jimdur.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diedari.jimdur.model.security.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

} 