package com.motos.jass.sistemalucky.socio.mapper;

import com.motos.jass.sistemalucky.socio.dto.SocioRequestDTO;
import com.motos.jass.sistemalucky.socio.dto.SocioResponseDTO;
import com.motos.jass.sistemalucky.socio.entity.Socio;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")

public interface SocioMapper {

    Socio toEntity(SocioRequestDTO dto);

    SocioResponseDTO toResponseDTO(Socio socio);
    // Para actualizar parcialmente un socio existente
    void updateSocioFromDto(SocioRequestDTO dto, @MappingTarget Socio socio);

}
