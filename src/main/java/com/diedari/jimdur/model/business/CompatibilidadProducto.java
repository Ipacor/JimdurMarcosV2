package com.diedari.jimdur.model.business;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "CompatibilidadProducto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompatibilidadProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "marca_compatible")
    @Size(max = 100, message = "La marca compatible no puede exceder 100 caracteres")
    private String marcaCompatible;

    @Column(name = "modelo_compatible")
    @Size(max = 100, message = "El modelo compatible no puede exceder 100 caracteres")
    private String modeloCompatible;

    @Column(name = "anio_desde")
    private Integer anioDesde;

    @Column(name = "anio_hasta")
    private Integer anioHasta;

    @Column(name = "descripcion_compatibilidad")
    @Size(max = 500, message = "La descripción de compatibilidad no puede exceder 500 caracteres")
    private String descripcionCompatibilidad;

    // Relación con Producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    @NotNull(message = "El producto es obligatorio")
    private Producto producto;
} 