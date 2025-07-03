package com.diedari.jimdur.repository.business;

import com.diedari.jimdur.model.business.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // No custom methods, use findAll() and filter in service
} 