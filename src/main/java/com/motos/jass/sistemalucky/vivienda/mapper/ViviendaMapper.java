package com.motos.jass.sistemalucky.vivienda.mapper;

import com.motos.jass.sistemalucky.vivienda.dto.ViviendaRequestDTO;
import com.motos.jass.sistemalucky.vivienda.dto.ViviendaResponseDTO;
import com.motos.jass.sistemalucky.vivienda.entity.Vivienda;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ViviendaMapper {

    // ✅ DTO → Entity (ignora sector porque lo asignas en el controller/service)
    @Mapping(target = "sector", ignore = true)
    Vivienda toEntity(ViviendaRequestDTO dto);

    // ✅ Entity → DTO (extrae id y nombre del sector)
    @Mapping(source = "sector.sectorId", target = "sectorId")
    @Mapping(source = "sector.nombreSector", target = "nombreSector")
    ViviendaResponseDTO toResponseDTO(Vivienda entity);

}
