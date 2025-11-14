package com.motos.jass.sistemalucky.socio.mapper;

import com.motos.jass.sistemalucky.socio.dto.RolRequestDTO;
import com.motos.jass.sistemalucky.socio.dto.RolResponseDTO;
import com.motos.jass.sistemalucky.socio.entity.Rol;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RolMapper {


    //  RequestDTO → Entity
    Rol toEntity(RolRequestDTO dto);

    //  Entity → ResponseDTO
    RolResponseDTO toResponseDTO(Rol entity);
}
