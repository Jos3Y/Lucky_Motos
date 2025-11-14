package com.motos.jass.sistemalucky.tecnico.mapper;

import com.motos.jass.sistemalucky.tecnico.dto.TecnicoResponseDTO;
import com.motos.jass.sistemalucky.tecnico.entity.DisponibilidadTecnico;
import com.motos.jass.sistemalucky.tecnico.entity.Tecnico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TecnicoMapper {
    
    TecnicoMapper INSTANCE = Mappers.getMapper(TecnicoMapper.class);
    
    @Mapping(target = "socioId", expression = "java(tecnico.getSocio() != null ? tecnico.getSocio().getId() : null)")
    @Mapping(target = "nombre", expression = "java(tecnico.getSocio() != null ? tecnico.getSocio().getNombre() : null)")
    @Mapping(target = "apellidos", expression = "java(tecnico.getSocio() != null ? tecnico.getSocio().getApellidos() : null)")
    @Mapping(target = "correo", expression = "java(tecnico.getSocio() != null ? tecnico.getSocio().getCorreo() : null)")
    @Mapping(target = "telefono", expression = "java(tecnico.getSocio() != null ? tecnico.getSocio().getTelefono() : null)")
    @Mapping(target = "estado", expression = "java(tecnico.getEstado().name())")
    @Mapping(target = "disponibilidades", expression = "java(mapDisponibilidades(tecnico.getDisponibilidades()))")
    TecnicoResponseDTO toResponseDTO(Tecnico tecnico);
    
    default List<TecnicoResponseDTO.DisponibilidadDTO> mapDisponibilidades(List<DisponibilidadTecnico> disponibilidades) {
        if (disponibilidades == null) return null;
        return disponibilidades.stream()
                .map(d -> {
                    TecnicoResponseDTO.DisponibilidadDTO dto = new TecnicoResponseDTO.DisponibilidadDTO();
                    dto.setId(d.getId());
                    dto.setDiaSemana(d.getDiaSemana().name());
                    dto.setHoraInicio(d.getHoraInicio().toString());
                    dto.setHoraFin(d.getHoraFin().toString());
                    dto.setDisponible(d.getDisponible());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}

