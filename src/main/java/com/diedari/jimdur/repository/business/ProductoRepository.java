package com.diedari.jimdur.repository.business;

import com.diedari.jimdur.model.business.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    List<Producto> findByActivoTrue();
    
    List<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId);
    
    List<Producto> findByMarcaIdAndActivoTrue(Long marcaId);
    
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND (p.nombre LIKE %:busqueda% OR p.descripcion LIKE %:busqueda%)")
    List<Producto> buscarPorNombreODescripcion(@Param("busqueda") String busqueda);
    
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.precio BETWEEN :precioMin AND :precioMax")
    List<Producto> buscarPorRangoPrecio(@Param("precioMin") Double precioMin, @Param("precioMax") Double precioMax);
    
    List<Producto> findByStockGreaterThanAndActivoTrue(Integer stock);
    
    Optional<Producto> findByNombreAndActivoTrue(String nombre);
} 