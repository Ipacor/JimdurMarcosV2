package com.diedari.jimdur.mapper;

import com.diedari.jimdur.dto.AgregarProveedorDTO;
import com.diedari.jimdur.dto.DireccionProveedorDTO;
import com.diedari.jimdur.model.business.DireccionProveedor;
import com.diedari.jimdur.model.business.Proveedor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ProveedorMapper {

    /**
     * Convierte un objeto {@link AgregarProveedorDTO} en una entidad
     * {@link Proveedor}.
     * <p>
     * Este método se utiliza para mapear los datos recibidos desde el cliente (DTO)
     * a la entidad persistente que será guardada en la base de datos.
     * </p>
     *
     * @param dto el objeto de transferencia de datos que contiene la información
     *            del proveedor a agregar
     * @return una instancia de {@link Proveedor} con los datos mapeados desde el
     *         DTO
     */
    public static Proveedor toEntity(AgregarProveedorDTO dto) {
        if (dto == null) {
            return null;
        }

        // Validar y formatear el correo electrónico
        String correo = dto.getEmailContacto();
        if (correo != null) {
            correo = correo.trim().toLowerCase();
        }

        Proveedor proveedor = Proveedor.builder()
                .id(dto.getIdProveedor())
                .nombre(dto.getNombreEmpresa())
                .ruc(dto.getRucEmpresa())
                .telefono(dto.getTelefonoContacto())
                .email(correo)
                .activo("true".equalsIgnoreCase(dto.getEstadoActivo()))
                .build();

        if (dto.getDirecciones() != null) {
            List<DireccionProveedor> direcciones = dto.getDirecciones().stream()
                    .map(d -> DireccionProveedor.builder()
                            .id(d.getIdDireccionProveedor())
                            .calle(d.getCalle())
                            .ciudad(d.getCiudad())
                            .estado(d.getDepartamentoEstado())
                            .codigoPostal(d.getCodigoPostal())
                            .pais(d.getPais())
                            .numero(d.getDistrito())
                            .tipoDireccion(d.getTipoDireccion())
                            .proveedor(proveedor)
                            .build())
                    .collect(Collectors.toList());
            proveedor.setDirecciones(direcciones);
        } else {
            proveedor.setDirecciones(Collections.emptyList());
        }

        return proveedor;
    }

    /**
     * Convierte una entidad {@link Proveedor} en un objeto
     * {@link AgregarProveedorDTO}.
     * <p>
     * Este método se utiliza para mapear los datos de una entidad persistente
     * a un objeto DTO que será utilizado en la capa de presentación (por ejemplo,
     * para mostrar en formularios o enviar al frontend).
     * </p>
     *
     * @param proveedor la entidad {@link Proveedor} que se desea convertir
     * @return una instancia de {@link AgregarProveedorDTO} con los datos del
     *         proveedor
     */
    public static AgregarProveedorDTO toDTO(Proveedor proveedor) {
        if (proveedor == null) {
            return null;
        }

        AgregarProveedorDTO dto = new AgregarProveedorDTO();
        dto.setIdProveedor(proveedor.getId());
        dto.setNombreEmpresa(proveedor.getNombre());
        dto.setRucEmpresa(proveedor.getRuc());
        dto.setTelefonoContacto(proveedor.getTelefono());
        dto.setEmailContacto(proveedor.getEmail());
        dto.setEstadoActivo(proveedor.getActivo() ? "true" : "false");

        if (proveedor.getDirecciones() != null) {
            List<DireccionProveedorDTO> direccionesDTO = proveedor.getDirecciones().stream()
                    .map(d -> {
                        DireccionProveedorDTO direccionDTO = new DireccionProveedorDTO();
                        direccionDTO.setIdDireccionProveedor(d.getId());
                        direccionDTO.setCalle(d.getCalle());
                        direccionDTO.setCiudad(d.getCiudad());
                        direccionDTO.setDepartamentoEstado(d.getEstado());
                        direccionDTO.setCodigoPostal(d.getCodigoPostal());
                        direccionDTO.setPais(d.getPais());
                        direccionDTO.setDistrito(d.getNumero());
                        direccionDTO.setTipoDireccion(d.getTipoDireccion());
                        return direccionDTO;
                    })
                    .collect(Collectors.toList());
            dto.setDirecciones(direccionesDTO);
        } else {
            dto.setDirecciones(Collections.emptyList());
        }

        return dto;
    }
}
