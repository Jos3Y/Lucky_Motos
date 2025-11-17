package com.motos.jass.sistemalucky.repuesto.mapper;

import com.motos.jass.sistemalucky.repuesto.dto.RepuestoRequestDTO;
import com.motos.jass.sistemalucky.repuesto.dto.RepuestoResponseDTO;
import com.motos.jass.sistemalucky.repuesto.entity.Repuesto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RepuestoMapper {
    
    RepuestoMapper INSTANCE = Mappers.getMapper(RepuestoMapper.class);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "citaRepuestos", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Repuesto toEntity(RepuestoRequestDTO dto);
    
    @Mapping(target = "estado", expression = "java(repuesto.getEstado() != null ? repuesto.getEstado().name() : null)")
    RepuestoResponseDTO toResponseDTO(Repuesto repuesto);
}

