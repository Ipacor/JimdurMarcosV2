package com.diedari.jimdur.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diedari.jimdur.model.business.Ubicaciones;
import com.diedari.jimdur.repository.business.UbicacionRepository;

@Service
public class UbicacionServiceImpl implements UbicacionService {

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Override
    public List<Ubicaciones> listarUbicaciones() {
        return ubicacionRepository.findAll();
    }

    @Override
    public Ubicaciones obtenerUbicacionPorId(Integer idUbicacion) {
        Optional<Ubicaciones> ubicacion = ubicacionRepository.findById(Long.valueOf(idUbicacion));
        return ubicacion.orElse(null); // Retorna null si no se encuentra
    }

    @Override
    public Ubicaciones guardarUbicacion(Ubicaciones ubicacion) {
        return ubicacionRepository.save(ubicacion);
    }

    @Override
    public void eliminarUbicacion(Integer idUbicacion) {
        ubicacionRepository.deleteById(Long.valueOf(idUbicacion));
    }

    @Override
    public Ubicaciones actualizarUbicacion(Ubicaciones ubicacion) {
        return ubicacionRepository.save(ubicacion); // save() también se usa para actualizar
    }
}
