package com.diedari.jimdur.model.business;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Boleta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Boleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_boleta", unique = true, nullable = false)
    @NotBlank(message = "El número de boleta es obligatorio")
    private String numeroBoleta;

    @Column(name = "fecha_emision", nullable = false)
    @NotNull(message = "La fecha de emisión es obligatoria")
    private LocalDateTime fechaEmision;

    @Column(name = "total", nullable = false)
    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.0", message = "El total no puede ser negativo")
    private Double total;

    @Column(name = "igv", nullable = false)
    @NotNull(message = "El IGV es obligatorio")
    @DecimalMin(value = "0.0", message = "El IGV no puede ser negativo")
    private Double igv;

    // Relación con Pedido
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false)
    @NotNull(message = "El pedido es obligatorio")
    private Pedido pedido;

    // Relación con DetalleBoleta
    @OneToMany(mappedBy = "boleta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleBoleta> detalles;
} 