package com.motos.jass.sistemalucky.cita.mapper;

import com.motos.jass.sistemalucky.cita.dto.CitaRequestDTO;
import com.motos.jass.sistemalucky.cita.dto.CitaResponseDTO;
import com.motos.jass.sistemalucky.cita.entity.Cita;
import com.motos.jass.sistemalucky.cita.entity.CitaRepuesto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CitaMapper {
    
    CitaMapper INSTANCE = Mappers.getMapper(CitaMapper.class);
    
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "moto", ignore = true)
    @Mapping(target = "tecnico", ignore = true)
    @Mapping(target = "tipoServicio", ignore = true)
    @Mapping(target = "repuestos", ignore = true)
    @Mapping(target = "codigoCita", ignore = true)
    Cita toEntity(CitaRequestDTO dto);
    
    CitaResponseDTO toResponseDTO(Cita cita);
    
    default CitaResponseDTO.ClienteDTO mapCliente(com.motos.jass.sistemalucky.cliente.entity.Cliente cliente) {
        if (cliente == null) return null;
        CitaResponseDTO.ClienteDTO dto = new CitaResponseDTO.ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setApellidos(cliente.getApellidos());
        dto.setDni(cliente.getDni());
        dto.setTelefono(cliente.getTelefono());
        dto.setCorreo(cliente.getCorreo());
        dto.setDireccion(cliente.getDireccion());
        return dto;
    }
    
    default CitaResponseDTO.MotoDTO mapMoto(com.motos.jass.sistemalucky.moto.entity.Moto moto) {
        if (moto == null) return null;
        CitaResponseDTO.MotoDTO dto = new CitaResponseDTO.MotoDTO();
        dto.setId(moto.getId());
        dto.setPlaca(moto.getPlaca());
        dto.setMarca(moto.getMarca());
        dto.setModelo(moto.getModelo());
        return dto;
    }
    
    default CitaResponseDTO.TecnicoDTO mapTecnico(com.motos.jass.sistemalucky.tecnico.entity.Tecnico tecnico) {
        if (tecnico == null) return null;
        CitaResponseDTO.TecnicoDTO dto = new CitaResponseDTO.TecnicoDTO();
        dto.setId(tecnico.getId());
        if (tecnico.getSocio() != null) {
            dto.setNombre(tecnico.getSocio().getNombre());
            dto.setApellidos(tecnico.getSocio().getApellidos());
        }
        dto.setEspecialidad(tecnico.getEspecialidad());
        dto.setEstado(tecnico.getEstado().name());
        return dto;
    }
    
    default CitaResponseDTO.TipoServicioDTO mapTipoServicio(com.motos.jass.sistemalucky.tiposervicio.entity.TipoServicio tipoServicio) {
        if (tipoServicio == null) return null;
        CitaResponseDTO.TipoServicioDTO dto = new CitaResponseDTO.TipoServicioDTO();
        dto.setId(tipoServicio.getId());
        dto.setNombre(tipoServicio.getNombre());
        dto.setDescripcion(tipoServicio.getDescripcion());
        dto.setPrecioBase(tipoServicio.getPrecioBase());
        return dto;
    }
    
    default List<CitaResponseDTO.RepuestoCitaDTO> mapRepuestos(List<CitaRepuesto> repuestos) {
        if (repuestos == null) return null;
        return repuestos.stream()
                .map(cr -> {
                    CitaResponseDTO.RepuestoCitaDTO dto = new CitaResponseDTO.RepuestoCitaDTO();
                    dto.setId(cr.getRepuesto().getId());
                    dto.setNombre(cr.getRepuesto().getNombre());
                    dto.setCantidad(cr.getCantidad());
                    dto.setPrecioUnitario(cr.getPrecioUnitario());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}

