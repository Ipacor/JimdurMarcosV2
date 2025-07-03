package com.diedari.jimdur.model.business;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "Direccion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "calle", nullable = false)
    @NotBlank(message = "La calle es obligatoria")
    @Size(max = 255, message = "La calle no puede exceder 255 caracteres")
    private String calle;

    @Column(name = "numero")
    @Size(max = 10, message = "El número no puede exceder 10 caracteres")
    private String numero;

    @Column(name = "ciudad", nullable = false)
    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 100, message = "La ciudad no puede exceder 100 caracteres")
    private String ciudad;

    @Column(name = "estado")
    @Size(max = 100, message = "El estado no puede exceder 100 caracteres")
    private String estado;

    @Column(name = "codigo_postal")
    @Size(max = 10, message = "El código postal no puede exceder 10 caracteres")
    private String codigoPostal;

    @Column(name = "pais", nullable = false)
    @NotBlank(message = "El país es obligatorio")
    @Size(max = 100, message = "El país no puede exceder 100 caracteres")
    private String pais;

    // Referencia por ID al usuario en la base de datos de seguridad
    @Column(name = "id_usuario", nullable = false)
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;

    // Relación con la entidad de referencia local (opcional, para consultas)
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "id_usuario", insertable = false, updatable = false)
    // private UsuarioRef usuarioRef;
} 