package com.diedari.jimdur.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DireccionProveedorDTO {

    private Long idDireccionProveedor;

    private String etiqueta;

    private String calle;

    private String distrito;

    private String ciudad;

    private String departamentoEstado;

    private String codigoPostal;

    private String pais;

    private String referencia;

    private String tipoDireccion;
}
