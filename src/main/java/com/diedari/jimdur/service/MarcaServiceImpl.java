package com.diedari.jimdur.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.diedari.jimdur.model.business.Marca;
import com.diedari.jimdur.repository.business.MarcaRepository;

@Service
public class MarcaServiceImpl implements MarcaService {

    @Autowired
    private MarcaRepository marcaRepository;

    @Override
    public Marca actualizarMarca(Marca marca) {
        return marcaRepository.save(marca);
    }

    @Override
    public void eliminarMarca(Long id) {
        marcaRepository.deleteById(id);
    }

    @Override
    public Marca guardarMarcaNuevo(Marca marca) {
        return marcaRepository.save(marca);
    }

    @Override
    public List<Marca> listarTodasLasMarcas() {
        return marcaRepository.findAll();
    }

    @Override
    public Marca obtenerMarcaPorId(Long id) {
        return marcaRepository.findById(id).orElse(null);
    }

    @Override
    public List<Marca> obtenerMarcaPorNombres(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return marcaRepository.findAll();
        }
        return marcaRepository.findAll().stream()
                .filter(m -> m.getNombre() != null && m.getNombre().equalsIgnoreCase(nombre))
                .collect(Collectors.toList());
    }

    @Override
    public List<Marca> obtenerMarcasPorEstado(Boolean activo) {
        return marcaRepository.findAll().stream()
                .filter(m -> m.getActivo() != null && m.getActivo().equals(activo))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Marca> obtenerMarcasFiltradas(String nombre, String estado, Pageable pageable) {
        List<Marca> marcas = marcaRepository.findAll();
        System.out.println("Total marcas en BD: " + marcas.size());
        System.out.println("Filtro nombre: '" + nombre + "'");
        System.out.println("Filtro estado: '" + estado + "'");
        if (nombre != null && !nombre.trim().isEmpty()) {
            marcas = marcas.stream()
                    .filter(m -> m.getNombre() != null && m.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                    .collect(Collectors.toList());
            System.out.println("Marcas después de filtro nombre: " + marcas.size());
        }
        if (estado != null && !estado.trim().isEmpty()) {
            if ("activa".equalsIgnoreCase(estado)) {
                marcas = marcas.stream()
                        .filter(m -> m.getActivo() != null && m.getActivo().equals(true))
                        .collect(Collectors.toList());
            } else if ("inactiva".equalsIgnoreCase(estado)) {
                marcas = marcas.stream()
                        .filter(m -> m.getActivo() != null && m.getActivo().equals(false))
                        .collect(Collectors.toList());
            }
            System.out.println("Marcas después de filtro estado: " + marcas.size());
        }
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), marcas.size());
        List<Marca> pageContent = marcas.subList(start, end);
        System.out.println("Marcas en página: " + pageContent.size());
        return new PageImpl<>(pageContent, pageable, marcas.size());
    }
}
