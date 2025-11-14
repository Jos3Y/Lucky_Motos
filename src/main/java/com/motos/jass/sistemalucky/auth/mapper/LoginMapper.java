package com.motos.jass.sistemalucky.auth.mapper;

import com.motos.jass.sistemalucky.auth.dto.LoginRequestDTO;
import com.motos.jass.sistemalucky.auth.dto.LoginResponseDTO;
import com.motos.jass.sistemalucky.socio.entity.Socio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")

public interface LoginMapper {

    LoginMapper INSTANCE = Mappers.getMapper(LoginMapper.class);


    Socio toEntity (LoginRequestDTO dto);


    @Mapping(target = "roles", expression = "java(mapRoles(socio))")
    @Mapping(source = "id", target = "idSocio")
    @Mapping(target = "token", ignore = true) // token lo vas a setear después

    LoginResponseDTO toResponseDTO(Socio socio);

    default List<String> mapRoles(Socio socio) {
        return socio.getRolSocios()
                .stream()
                .filter(rolSocio -> {
                    String estado = rolSocio.getEstado() != null ? rolSocio.getEstado() : "";
                    return "ACTIVO".equalsIgnoreCase(estado) || "Activo".equalsIgnoreCase(estado);
                })
                .map(rolSocio -> rolSocio.getRol().getDescripcion())
                .collect(Collectors.toList());
    }


}
