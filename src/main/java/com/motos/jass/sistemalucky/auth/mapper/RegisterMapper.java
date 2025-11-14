package com.motos.jass.sistemalucky.auth.mapper;

import com.motos.jass.sistemalucky.auth.dto.RegisterRequestDTO;
import com.motos.jass.sistemalucky.auth.dto.RegisterResponseDTO;
import com.motos.jass.sistemalucky.socio.entity.Socio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface   RegisterMapper {
    RegisterMapper INSTANCE = Mappers.getMapper(RegisterMapper.class);

    // Mapea el DTO de request a entidad
    Socio toEntity(RegisterRequestDTO dto);

    // Mapea entidad a  DTO de response
    @Mapping(target = "roles", expression = "java(mapRoles(socio))")
    RegisterResponseDTO toResponseDTO(Socio socio);

    // Método por defecto para extraer los nombres de roles
    default List<String> mapRoles(Socio socio) {
        return socio.getRolSocios()
                .stream()
                .map(rolSocio -> rolSocio.getRol().getDescripcion())
                .collect(Collectors.toList());
    }

}
