package com.diedari.jimdur.model.business;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "monto", nullable = false)
    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.0", message = "El monto no puede ser negativo")
    private Double monto;

    @Column(name = "fecha_pago", nullable = false)
    @NotNull(message = "La fecha de pago es obligatoria")
    private LocalDateTime fechaPago;

    @Column(name = "metodo_pago", nullable = false)
    @NotBlank(message = "El método de pago es obligatorio")
    private String metodoPago;

    @Column(name = "estado", nullable = false)
    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    // Relación con Pedido
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false)
    @NotNull(message = "El pedido es obligatorio")
    private Pedido pedido;
} 