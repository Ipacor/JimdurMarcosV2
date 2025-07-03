package com.diedari.jimdur.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.diedari.jimdur.model.Categoria;

public interface CategoriaService {
    List<Categoria> obtenerTodasLasCategorias(); // Obtener todas las categorías

    Categoria obtenerCategoriaPorId(Long id); // Obtener una categoría por ID

    Categoria crearCategoria(Categoria categoria); // Crear una nueva categoría

    Categoria actualizarCategoria(Categoria categoria); // Actualizar una categoría existente

    void eliminarCategoriaPorId(Long id); // Eliminar una categoría por ID

    List<Categoria> obtenerCategoriaPorEstado(boolean activa); // Obtener una categoría por estado (activa/inactiva)

    List<Categoria> obtenerCategoriaPorNombre(String nombre); // Obtener una categoría por nombre

    // Método para obtener categorías filtradas con paginación
    Page<Categoria> obtenerCategoriasFiltradas(
        String nombreCategoria,
        String estadoCategoria,
        Pageable pageable
    );
}