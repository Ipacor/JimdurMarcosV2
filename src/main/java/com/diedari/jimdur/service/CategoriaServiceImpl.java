package com.diedari.jimdur.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.diedari.jimdur.model.business.Categoria;
import com.diedari.jimdur.repository.business.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    // Inyectar el repositorio de categoría
    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public List<Categoria> obtenerTodasLasCategorias() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria obtenerCategoriaPorId(Long id) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(id);
        return categoriaOpt.orElse(null);  // Devuelve null si no se encuentra la categoría
    }

    @Override
    public Categoria crearCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    public Categoria actualizarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    public void eliminarCategoriaPorId(Long id) {
        categoriaRepository.deleteById(id);
    }

    @Override
    public List<Categoria> obtenerCategoriaPorEstado(boolean activa) {
        return categoriaRepository.findAll().stream()
                .filter(c -> c.getActivo() == activa)
                .collect(Collectors.toList());
    }

    @Override
    public List<Categoria> obtenerCategoriaPorNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return categoriaRepository.findAll();
        }
        return categoriaRepository.findAll().stream()
                .filter(c -> c.getNombre() != null && c.getNombre().equalsIgnoreCase(nombre))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Categoria> obtenerCategoriasFiltradas(String nombreCategoria, String estadoCategoria, Pageable pageable) {
        List<Categoria> categorias = categoriaRepository.findAll();
        if (nombreCategoria != null && !nombreCategoria.isEmpty()) {
            categorias = categorias.stream()
                    .filter(c -> c.getNombre() != null && c.getNombre().toLowerCase().contains(nombreCategoria.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (estadoCategoria != null && !estadoCategoria.isEmpty()) {
            boolean activa = "activa".equalsIgnoreCase(estadoCategoria);
            categorias = categorias.stream()
                    .filter(c -> c.getActivo() == activa)
                    .collect(Collectors.toList());
        }
        // paginar manualmente
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), categorias.size());
        List<Categoria> pageContent = categorias.subList(start, end);
        return new org.springframework.data.domain.PageImpl<>(pageContent, pageable, categorias.size());
    }
}
