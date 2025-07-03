package com.diedari.jimdur.model.business;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "ProductoProveedor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoProveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_producto", nullable = false)
    @NotNull(message = "El ID del producto es obligatorio")
    private Long idProducto;

    @Column(name = "id_proveedor", nullable = false)
    @NotNull(message = "El ID del proveedor es obligatorio")
    private Long idProveedor;

    @Column(name = "codigo_proveedor")
    @Size(max = 50, message = "El código del proveedor no puede exceder 50 caracteres")
    private String codigoProveedor;

    @Column(name = "precio_compra", nullable = false)
    @NotNull(message = "El precio de compra es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio de compra no puede ser negativo")
    private Double precioCompra;

    @Column(name = "stock_disponible")
    @Min(value = 0, message = "El stock disponible no puede ser negativo")
    private Integer stockDisponible;

    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true;

    // Relación con Producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", insertable = false, updatable = false)
    private Producto producto;

    // Relación con Proveedor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor", insertable = false, updatable = false)
    private Proveedor proveedor;
} 