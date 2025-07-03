package com.diedari.jimdur.model.business;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "EspecificacionProducto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EspecificacionProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @Column(name = "valor", nullable = false)
    @NotBlank(message = "El valor es obligatorio")
    @Size(max = 255, message = "El valor no puede exceder 255 caracteres")
    private String valor;

    @Column(name = "descripcion")
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @Column(name = "orden")
    private Integer orden;

    // Relación con Producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    @NotNull(message = "El producto es obligatorio")
    private Producto producto;
} 