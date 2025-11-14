package com.motos.jass.sistemalucky.socio.mapper;


import com.motos.jass.sistemalucky.socio.dto.RolSocioRequestDTO;
import com.motos.jass.sistemalucky.socio.dto.RolSocioResponseDTO;
import com.motos.jass.sistemalucky.socio.entity.RolSocio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface RolSocioMapper {

    // RequestDTO → Entity
    @Mapping(target = "socio", ignore = true)
    @Mapping(target = "rol", ignore = true)

    RolSocio toEntity(RolSocioRequestDTO dto);

    // Entity → ResponseDTO
    @Mapping(source = "socio.nombre", target = "socioNombre")
    @Mapping(source = "socio.apellidos", target = "socioApellido")
    @Mapping(source = "socio.correo", target = "correoSocio")

    @Mapping(source = "rol.descripcion", target = "rolNombre")
    RolSocioResponseDTO toResponseDTO(RolSocio entity);
}
