package com.diedari.jimdur.repository.business;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diedari.jimdur.model.business.EspecificacionProducto;

@Repository
public interface EspecificacionProductoRepository extends JpaRepository<EspecificacionProducto, Long> {

} 