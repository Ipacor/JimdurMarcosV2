package com.diedari.jimdur.repository.business;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diedari.jimdur.model.business.Direccion;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Long> {

} 