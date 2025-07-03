package com.diedari.jimdur.model.business;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad de referencia para Usuario en la base de datos de negocio.
 * Esta entidad solo contiene el ID y datos básicos necesarios para el negocio,
 * sin las relaciones de seguridad.
 */
@Entity
@Table(name = "Usuario_Ref")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRef {

    @Id
    private Long id; // Mismo ID que en la base de datos de seguridad

    @Column(name = "nombre", nullable = false)
    private String nombres;

    @Column(name = "apellidos", nullable = false)
    private String apellidos;

    @Column(name = "correo", unique = true, nullable = false)
    private String email;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "estado_cuenta", nullable = false)
    private String estadoCuenta;

    @Column(name = "fecha_registro", nullable = false)
    private java.time.LocalDateTime fechaRegistro;

    // Método para sincronizar con la entidad de seguridad
    public void sincronizarDesdeSeguridad(com.diedari.jimdur.model.security.Usuario usuario) {
        this.id = usuario.getId();
        this.nombres = usuario.getNombres();
        this.apellidos = usuario.getApellidos();
        this.email = usuario.getEmail();
        this.telefono = usuario.getTelefono();
        this.estadoCuenta = usuario.getEstadoCuenta();
        this.fechaRegistro = usuario.getFechaRegistro();
    }
} 