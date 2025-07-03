package com.diedari.jimdur.model.business;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Long idPedido;
    
    @Column(name = "fecha", nullable = false)
    @NotNull(message = "La fecha es obligatoria")
    private LocalDateTime fecha;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    @NotNull(message = "El estado es obligatorio")
    private EstadoPedido estado;
    
    @Column(name = "total", nullable = false)
    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.0", message = "El total no puede ser negativo")
    private Double total;
    
    // Referencia por ID al usuario en la base de datos de seguridad
    @Column(name = "id_usuario", nullable = false)
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_direccion", nullable = false)
    @NotNull(message = "La dirección es obligatoria")
    private Direccion direccion;
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetallePedido> detalles;
    
    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Pago pago;
    
    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Boleta boleta;
    
    public enum EstadoPedido {
        pendiente, pagado, enviado, entregado, cancelado
    }
} 