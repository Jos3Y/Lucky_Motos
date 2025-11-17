package com.motos.jass.sistemalucky.tiposervicio.mapper;

import com.motos.jass.sistemalucky.tiposervicio.dto.TipoServicioRequestDTO;
import com.motos.jass.sistemalucky.tiposervicio.dto.TipoServicioResponseDTO;
import com.motos.jass.sistemalucky.tiposervicio.entity.TipoServicio;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TipoServicioMapper {
    TipoServicioResponseDTO toDto(TipoServicio tipoServicio);
    TipoServicio toEntity(TipoServicioRequestDTO requestDTO);
}

