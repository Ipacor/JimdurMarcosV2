package com.diedari.jimdur.model.business;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "Marca")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", unique = true, nullable = false)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @Column(name = "descripcion")
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "pais_origen")
    private String paisOrigen;

    @Column(name = "sitio_web")
    private String sitioWeb;

    // Relación con Productos
    @OneToMany(mappedBy = "marca", fetch = FetchType.LAZY)
    private List<Producto> productos;
} 