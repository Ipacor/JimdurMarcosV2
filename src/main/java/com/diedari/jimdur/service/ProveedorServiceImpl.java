package com.diedari.jimdur.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.diedari.jimdur.model.business.Proveedor;
import com.diedari.jimdur.repository.business.ProveedorRepository;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Override
    public List<Proveedor> listarProveedores() {
        return proveedorRepository.findAll();
    }

    @Override
    public Proveedor obtenerProveedorPorId(Long idProveedor) {
        return proveedorRepository.findById(idProveedor).orElse(null);
    }

    @Override
    public Proveedor guardarProveedor(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Override
    public void eliminarProveedor(Long idProveedor) {
        proveedorRepository.deleteById(idProveedor);
    }

    @Override
    public Proveedor actualizarProveedor(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Override
    public List<Proveedor> obtenerTodosLosProveedores() {
        return proveedorRepository.findAll();
    }

    @Override
    public List<Proveedor> obtenerProveedoresActivos(String estadoActivo) {
        boolean activo = "Activo".equalsIgnoreCase(estadoActivo);
        return proveedorRepository.findAll().stream()
            .filter(p -> p.getActivo() != null && p.getActivo() == activo)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Proveedor> obtenerProveedoresFiltrados(
            String nombreProveedor,
            String tipoProveedor,
            String estadoActivo,
            Pageable pageable) {
        List<Proveedor> proveedores = proveedorRepository.findAll();
        if (nombreProveedor != null && !nombreProveedor.isEmpty()) {
            proveedores = proveedores.stream()
                .filter(p -> p.getNombre() != null && p.getNombre().toLowerCase().contains(nombreProveedor.toLowerCase()))
                .collect(Collectors.toList());
        }
        if (estadoActivo != null && !estadoActivo.isEmpty()) {
            boolean activo = "Activo".equalsIgnoreCase(estadoActivo);
            proveedores = proveedores.stream()
                .filter(p -> p.getActivo() != null && p.getActivo() == activo)
                .collect(Collectors.toList());
        }
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), proveedores.size());
        List<Proveedor> pageContent = proveedores.subList(start, end);
        return new PageImpl<>(pageContent, pageable, proveedores.size());
    }
}
