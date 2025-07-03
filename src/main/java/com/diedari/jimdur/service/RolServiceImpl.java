package com.diedari.jimdur.service;

import com.diedari.jimdur.model.security.Rol;
import com.diedari.jimdur.model.security.Permiso;
import com.diedari.jimdur.repository.security.RolRepository;
import com.diedari.jimdur.repository.security.PermisoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rol> findAllActivos() {
        return rolRepository.findAll().stream()
            .filter(r -> r.getActivo() != null && r.getActivo())
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rol> findById(Long id) {
        return rolRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rol> findByNombre(String nombre) {
        return rolRepository.findAll().stream()
            .filter(r -> r.getNombre() != null && r.getNombre().equals(nombre))
            .findFirst();
    }

    @Override
    public Rol save(Rol rol) {
        if (rol.getActivo() == null) {
            rol.setActivo(true);
        }
        return rolRepository.save(rol);
    }

    @Override
    public void deleteById(Long id) {
        rolRepository.deleteById(id);
    }

    @Override
    public void activar(Long id) {
        Optional<Rol> rolOpt = rolRepository.findById(id);
        if (rolOpt.isPresent()) {
            Rol rol = rolOpt.get();
            rol.setActivo(true);
            rolRepository.save(rol);
        }
    }

    @Override
    public void desactivar(Long id) {
        Optional<Rol> rolOpt = rolRepository.findById(id);
        if (rolOpt.isPresent()) {
            Rol rol = rolOpt.get();
            rol.setActivo(false);
            rolRepository.save(rol);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNombre(String nombre) {
        return rolRepository.findAll().stream()
            .anyMatch(r -> r.getNombre() != null && r.getNombre().equals(nombre));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNombreAndIdNot(String nombre, Long id) {
        return rolRepository.findAll().stream()
            .anyMatch(r -> r.getNombre() != null && r.getNombre().equals(nombre) && !r.getId().equals(id));
    }

    @Override
    public Rol asignarPermisos(Long rolId, Set<Long> permisosIds) {
        Optional<Rol> rolOpt = rolRepository.findById(rolId);
        if (rolOpt.isPresent()) {
            Rol rol = rolOpt.get();
            
            // Obtener los permisos por IDs
            Set<Permiso> permisos = new HashSet<>();
            if (permisosIds != null && !permisosIds.isEmpty()) {
                permisos = permisoRepository.findAllById(permisosIds)
                    .stream()
                    .collect(Collectors.toSet());
            }
            
            rol.setPermisos(permisos);
            return rolRepository.save(rol);
        }
        throw new RuntimeException("Rol no encontrado con ID: " + rolId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Permiso> getPermisosDisponibles() {
        return permisoRepository.findAll();
    }
} 