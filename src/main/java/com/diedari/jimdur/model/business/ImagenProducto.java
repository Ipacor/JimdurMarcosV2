package com.diedari.jimdur.model.business;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "ImagenProducto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImagenProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idImagen;

    @Column(name = "nombre_archivo", nullable = false)
    @NotBlank(message = "El nombre del archivo es obligatorio")
    private String nombreArchivo;

    @Column(name = "descripcion")
    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    private String descripcion;

    @Column(name = "es_portada", nullable = false)
    @Builder.Default
    private Boolean esPortada = false;

    @Column(name = "orden")
    private Integer orden;

    // Relación con Producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    @NotNull(message = "El producto es obligatorio")
    private Producto producto;
} 