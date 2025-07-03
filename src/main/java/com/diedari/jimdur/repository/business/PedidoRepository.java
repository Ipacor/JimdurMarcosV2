package com.diedari.jimdur.repository.business;

import com.diedari.jimdur.model.business.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    List<Pedido> findByIdUsuarioOrderByFechaDesc(Long idUsuario);
    
    List<Pedido> findByEstadoOrderByFechaDesc(Pedido.EstadoPedido estado);
    
    @Query("SELECT p FROM Pedido p WHERE p.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<Pedido> findByFechaBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                   @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT p FROM Pedido p WHERE p.idUsuario = :idUsuario AND p.estado = :estado")
    List<Pedido> findByIdUsuarioAndEstado(@Param("idUsuario") Long idUsuario, 
                                         @Param("estado") Pedido.EstadoPedido estado);
    
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.estado = :estado")
    Long countByEstado(@Param("estado") Pedido.EstadoPedido estado);
    
    @Query("SELECT SUM(p.total) FROM Pedido p WHERE p.fecha BETWEEN :fechaInicio AND :fechaFin")
    Double sumTotalByFechaBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                 @Param("fechaFin") LocalDateTime fechaFin);
} 